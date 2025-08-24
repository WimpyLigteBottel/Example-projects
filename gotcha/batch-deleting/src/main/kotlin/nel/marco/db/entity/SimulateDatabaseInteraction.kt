package nel.marco.db.entity

import nl.wykorijnsburger.kminrandom.minRandom
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit

@Component
open class SimulateDatabaseInteraction(
    private val customerRepo: CustomerRepo,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MILLISECONDS)
    open fun insertRecord() = runCatching {
        customerRepo.saveAndFlush(minRandom<Customer>().copy(name = minRandom()))

    }

    @Scheduled(fixedRate = 50, timeUnit = TimeUnit.MILLISECONDS)
    open fun updateRecord() = runCatching {
        val customer = customerRepo.findRandomCustomer().copy(name = minRandom())
        customerRepo.saveAndFlush(customer)
    }

    @Scheduled(fixedDelay = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Transactional
    open fun delete() = runCatching {
        customerRepo.deleteInBatch(OffsetDateTime.now(), 10)
    }
}