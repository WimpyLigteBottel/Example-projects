package nel.marco.service

import nel.marco.api.Order
import nel.marco.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val repository: OrderRepository
) {

    fun getOrder(id: Long): Order {
        println(
            "Service: ${Thread.currentThread().name}"
        )

        return repository.findById(id)
    }
}