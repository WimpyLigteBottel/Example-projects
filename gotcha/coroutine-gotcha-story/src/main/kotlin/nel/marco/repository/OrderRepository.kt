package nel.marco.repository

import nel.marco.api.Order
import nel.marco.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep

@Repository
class OrderRepository {

    val logger = LoggerFactory.getLogger(OrderService::class.java)

    suspend fun findById(id: Long): Order {
        // Lesson 1: Blocking code
        sleep(1_000)

        logger.info("Repository: thread=${Thread.currentThread().name} - id=${id}")

        return Order(
            id = id,
            customerId = 123,
            description = "Kotlin conference ticket"
        )
    }


}