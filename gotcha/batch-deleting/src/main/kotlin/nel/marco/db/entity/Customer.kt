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
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime
import java.util.*
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
            where id in (SELECT id FROM Customer WHERE created < :createdDate)
        """
    )
    fun deleteInBatch(createdDate: OffsetDateTime): Int


}

@RestController
@EnableScheduling
open class CustomController(
    private val customerRepo: CustomerRepo,
    private val jdbcTemplate: JdbcTemplate
) {

    @GetMapping
    fun helloWorld(): String {
        return "hello world!"
    }

    @GetMapping("/v1/find")
    open fun findCustomer(): MutableList<Customer> {

        return customerRepo.findAll().toMutableList()
    }

    @GetMapping("/v1/size")
    open fun size(): Int {
        return customerRepo.findSizeOfCustomers()
    }

    @GetMapping("/v1/delete")
    @Transactional
    open fun delete(): String {
        val time = measureTimeMillis {
            customerRepo.deleteInBatch(OffsetDateTime.now())
        }
        return "$time ms"
    }


    @GetMapping("/v1/batch")
    @Transactional
    open fun createBatch(@RequestParam size: Int = 1000): Int {

        val customers = (1..size).map {
            val result = minRandom<Customer>()
            result.name = minRandom()
            result.id
            result
        }
        val time = measureTimeMillis {
            batchInsert(customers)
        }
        println( "$time ms")

        return customerRepo.findSizeOfCustomers()
    }


    fun batchInsert(customers: List<Customer>) {
        val sql = "INSERT INTO customer (id,name, created, updated) VALUES (?,?, ?, ?)"

        val batchArgs = customers.map { customer ->
            arrayOf(UUID.randomUUID(), customer.name, customer.created, customer.updated)
        }

        jdbcTemplate.batchUpdate(sql, batchArgs)
    }
}
