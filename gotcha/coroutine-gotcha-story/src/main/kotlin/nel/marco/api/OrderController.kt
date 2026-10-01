package nel.marco.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import nel.marco.service.OrderService
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Thread.sleep
import java.util.concurrent.Executors

data class Order(
    val id: Long,
    val customerId: Long,
    val description: String
)


@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {

    val logger = LoggerFactory.getLogger(OrderController::class.java)

    @GetMapping("/{id}/lesson1")
    suspend fun getOrder(
        @PathVariable id: Long
    ): Order {
        logger.info("Controller: thread=${Thread.currentThread().name}")


        return orderService.getOrderLesson1(id)
    }

    @GetMapping("/{id}/lesson2")
    suspend fun getOrderLesson2(
        @PathVariable id: Long
    ): Unit = coroutineScope {
        // Lesson 2: CONTEXT
        MDC.put("LESSON-2", "<BLANK>")

        logger.info("LESSON2: thread=${Thread.currentThread().name}; MDC={}", MDC.get("LESSON-2"))

        val msg1 = async(Dispatchers.IO) {
            logger.info("       IO coroutine: thread=${Thread.currentThread().name}; MDC={}", MDC.get("LESSON-2"))
        }

        val msg2 = async {
            logger.info(
                "       DEFAULT coroutine: thread=${Thread.currentThread().name}; MDC={}", MDC.get("LESSON-2")
            )
        }
    }


    val dispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()

    @GetMapping("/{id}/lesson3")
    suspend fun getOrderLesson3(
        @PathVariable id: Long
    ): Unit = coroutineScope {
        logger.info("START ${Thread.currentThread().name}")

        withContext(dispatcher) {
            sleep(2000)
            logger.info("END ${Thread.currentThread().name}")
        }
    }

}