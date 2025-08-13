package nel.marco.db.entity

import jakarta.persistence.*
import nl.wykorijnsburger.kminrandom.minRandom
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.system.measureTimeMillis


@Entity
@Table
class Customer {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    var id: UUID? = null

    @Column
    var name: String? = null

    @CreationTimestamp
    var created: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    var updated: OffsetDateTime = OffsetDateTime.now()

}

@Repository
interface CustomerRepo : JpaRepository<Customer, Long> {

    @Query(
        "select count(*) from Customer",
    )
    fun findSizeOfCustomers(): Int

    @Modifying
    @Query(
        """
            delete from Customer
            where id in (
                SELECT id FROM Customer
                WHERE created < :createdDate
                LIMIT :limit
            )
        """,
        nativeQuery = true
    )
    fun deleteInBatch(createdDate: OffsetDateTime, limit: Long): Int

}

@RestController
@EnableScheduling
open class CustomController(
    private val customerRepo: CustomerRepo,
    private val batchService: BatchService
) {

    @GetMapping("/v1/size")
    open fun size(): Int {
        return customerRepo.findSizeOfCustomers()
    }

    @GetMapping("/v1/delete")
    open fun delete(@RequestParam batches: Boolean = true): String {
        val time = measureTimeMillis {
            batchService.batchDelete(batches)
        }

        println(
            """
            delete (batches=${batches}) took $time ms
        """.trimIndent()
        )
        return "$time ms"
    }


    @GetMapping("/v1/batch")
    open fun createBatch(@RequestParam size: Int = 100000): Int {
        val customers = (1..size).map {
            val result = minRandom<Customer>()
            result.name = minRandom()
            result.id
            result
        }
        val time = measureTimeMillis {
            customers.chunked(10000).map {
                CompletableFuture.supplyAsync({
                    batchService.batchInsert(it)

                })
            }.forEach {
                it.join()
            }

        }
        println("create took $time ms")

        return customerRepo.findSizeOfCustomers()
    }


}


@Service
open class BatchService(
    private val jdbcTemplate: JdbcTemplate,
    private val customerRepo: CustomerRepo,
) {

    @Transactional
    open fun batchInsert(customers: List<Customer>) {
        val sql = "INSERT INTO customer (id,name, created, updated) VALUES (?,?, ?, ?)"

        val batchArgs = customers.map { customer ->
            arrayOf(UUID.randomUUID(), customer.name, customer.created, customer.updated)
        }

        jdbcTemplate.batchUpdate(sql, batchArgs)
    }

    @Transactional
    open fun batchDelete(deleteSmallerBatches: Boolean) {
        if (deleteSmallerBatches) {
            (1..10).map {
                customerRepo.deleteInBatch(OffsetDateTime.now(), 10000)
            }

        } else {
            customerRepo.deleteInBatch(OffsetDateTime.now(),100000)
        }
    }
}