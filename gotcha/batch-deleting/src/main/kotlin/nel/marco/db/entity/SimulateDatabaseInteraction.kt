package nel.marco.db.entity

import nl.wykorijnsburger.kminrandom.minRandom
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
open class SimulateDatabaseInteraction(
    private val customerRepo: CustomerRepo
) {

    val insertTimes = 10000
    val updateTimes = 1000
    val deleteTimes = 100

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    open fun insertRecord() {
        repeat(insertTimes) {
            val result = minRandom<Customer>()
            result.name = minRandom()
            customerRepo.save(result)
            customerRepo.flush()

        }
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    open fun updateRecord() {
        repeat(updateTimes) {
            val customer = customerRepo.findRandomCustomer()
            customer.name = minRandom()
            customerRepo.save(customer)
            customerRepo.flush()

        }
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    open fun deleteRecord() {
        repeat(deleteTimes) {
            val customer = customerRepo.findRandomCustomer()
            customerRepo.delete(customer)
            customerRepo.flush()
        }
    }
}