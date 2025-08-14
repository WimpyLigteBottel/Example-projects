package nel.marco.db.entity

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
        runBlocking(Dispatchers.IO) {
            repeat(insertTimes) {
                launch {
                    customerRepo.saveAndFlush(minRandom<Customer>().copy(name = minRandom()))
                }
            }
        }

    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    open fun updateRecord() {

        runBlocking(Dispatchers.IO) {
            repeat(updateTimes) {
                launch {
                    val customer = customerRepo.findRandomCustomer().copy(name = minRandom())
                    customerRepo.saveAndFlush(customer)
                }
            }
        }
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    open fun deleteRecord() {
        runBlocking(Dispatchers.IO) {
            repeat(deleteTimes) {
                launch {
                    val customer = customerRepo.findRandomCustomer()
                    customerRepo.delete(customer)
                    customerRepo.flush()
                }
            }
        }
    }
}