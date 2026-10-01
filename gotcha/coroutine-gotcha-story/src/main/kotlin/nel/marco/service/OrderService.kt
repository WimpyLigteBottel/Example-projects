package nel.marco.service

import kotlinx.coroutines.*
import nel.marco.api.Order
import nel.marco.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val repository: OrderRepository
) {

    suspend fun getOrder(id: Long): Order = coroutineScope {
        println(
            "Service: ${Thread.currentThread().name}"
        )

        delay(1_000)

        val order1 = async(Dispatchers.IO) { repository.findById(id) }
        val order2 = async() { repository.findById(id) }


        order1.await()
        order2.await()
    }
}