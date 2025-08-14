package nel.marco.db.entity

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.UUID

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
    open fun batchDelete(deleteSmallerBatches: Boolean): Int {
        val totalAmountToDeleted = 100000
        return if (deleteSmallerBatches) {
            val batchCount = 10
            (1..batchCount).sumOf {
                customerRepo.deleteInBatch(OffsetDateTime.now(), (totalAmountToDeleted / batchCount).toLong())
            }
        } else {
            customerRepo.deleteInBatch(OffsetDateTime.now(), 100000)
        }
    }
}