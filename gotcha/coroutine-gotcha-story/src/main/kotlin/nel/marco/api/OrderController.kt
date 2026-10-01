package nel.marco.api

import nel.marco.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

    @GetMapping("/{id}")
    suspend fun getOrder(
        @PathVariable id: Long
    ): Order {


        logger.info("Controller: thread=${Thread.currentThread().name}")


        return orderService.getOrder(id)
    }
}