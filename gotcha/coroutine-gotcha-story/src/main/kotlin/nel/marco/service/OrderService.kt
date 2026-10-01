package nel.marco.service

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import nel.marco.api.Order
import nel.marco.repository.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.Thread.sleep

@Service
class OrderService(
    private val repository: OrderRepository
) {

    val logger = LoggerFactory.getLogger(OrderService::class.java)

    suspend fun getOrderLesson1(id: Long): Order =
        coroutineScope {
            logger.info("Service: thread=${Thread.currentThread().name}")

            val order1 = async {
                repository.findById(id)
            }

            val order2 = async {
                repository.findById(9999)
            }

            order1.await()
            order2.await()
        }
}