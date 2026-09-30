package nel.marco.repository

import nel.marco.api.Order
import org.springframework.stereotype.Repository

@Repository
class OrderRepository {

    fun findById(id: Long): Order {
        println(
            "DB START: ${Thread.currentThread().name}"
        )

        Thread.sleep(1_000)

        println(
            "DB END: ${Thread.currentThread().name}"
        )

        return Order(
            id = id,
            customerId = 123,
            description = "Kotlin conference ticket"
        )
    }
}