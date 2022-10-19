package nel.marco.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
class BaseTestController(private val serviceCallOne: ServiceCallOne, private val serviceCallTwo: ServiceCallTwo) {

    val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/")
    fun landingPage(): String {

        try {
            log.info("Starting coroutine")

            val result = runBlocking {
                val answer1 = async(Dispatchers.IO) {
                    log.info("serviceCallOne started")
                    serviceCallOne.xSecondCall(4)
                }
                val answer2 = async(Dispatchers.IO) {
                    log.info("ServiceCallTwo started")
                    serviceCallTwo.xSecondCall(4000)
                }
                val await = answer1.await()
                log.info("serviceCallOne done")
                val await1 = answer2.await()
                log.info("ServiceCallTwo done")
                listOf(await, await1)

            }
            log.info("Done coroutine")

            return "DONE! ${result.sum()}"
        } catch (e: Exception) {
            log.error("BaseTestController.landingPage() failed!!!!", e)
            return "FAILED!"
        }
    }


}

@Service
open class ServiceCallOne {

    val log = LoggerFactory.getLogger(ServiceCallOne::class.java)

    fun xSecondCall(totalAmount: Long = 1L): Long {
        return totalAmount
    }
}

@Service
open class ServiceCallTwo {

    val log = LoggerFactory.getLogger(ServiceCallTwo::class.java)

    @Retryable(maxAttempts = 10, backoff = Backoff(value = 10))
    open fun xSecondCall(totalAmount: Long = 1L): Long {
        Thread.sleep(totalAmount)
        retryMethod()
        return totalAmount
    }

    public open fun retryMethod() {
        if (Random.nextInt(100) > 30) {
            log.warn("ServiceCallTwo failed")
            throw Exception("HELLO EXCEPTION")
        }
    }
}