package nel.marco.repository

import kotlinx.coroutines.coroutineScope
import nel.marco.api.Order
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep

@Repository
class OrderRepository {

    suspend fun findById(id: Long): Order = coroutineScope {
        println(
            "DB START: ${Thread.currentThread().name}"
        )

        // Lesson 1: Blocking code
        sleep(1_000)

        println(
            "DB END: ${Thread.currentThread().name}"
        )

        Order(
            id = id,
            customerId = 123,
            description = "Kotlin conference ticket"
        )
    }
}