package nel.marco.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import nel.marco.api.Order
import nel.marco.repository.OrderRepository
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val repository: OrderRepository
) {

    val logger = LoggerFactory.getLogger(OrderService::class.java)

    suspend fun getOrder(id: Long): Order =
        coroutineScope {
            // Lesson 2: CONTEXT
            MDC.put("LESSON-2", "<BLANK>")

            logger.info("Service: thread=${Thread.currentThread().name}; MDC={}", MDC.get("LESSON-2"))

            val order1 = async(Dispatchers.IO) {
                logger.info("       IO coroutine: thread=${Thread.currentThread().name}; MDC={}", MDC.get("LESSON-2"))

                repository.findById(id)
            }

            val order2 = async {
                logger.info(
                    "       DEFAULT coroutine: thread=${Thread.currentThread().name}; MDC={}",
                    MDC.get("LESSON-2")
                )

                repository.findById(9999)
            }

            order1.await()
            order2.await()
        }
}