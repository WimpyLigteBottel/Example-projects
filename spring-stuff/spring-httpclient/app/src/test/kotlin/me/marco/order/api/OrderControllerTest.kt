package me.marco.order.api

import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
)
class OrderControllerTest {
    @Autowired
    lateinit var orderClient: OrderClient

    @Test
    fun `creating an order`() {
        val response = orderClient.createOrder(CreateOrderRequest("zxc"))

        when (response.body) {
            is OrderResponse.OK -> {
                println(response.body)
            }

            is OrderResponse.Problem -> fail("Is problem")
            null -> fail("is null response")
            is OrderResponse.Accepted -> TODO()
        }

        println(response)
    }

    @Test
    fun `get an order`() {
        val response = orderClient.getOrder("123")

        when (response.body) {
            is OrderResponse.OK -> {
                println(response.body)
            }
            is OrderResponse.Problem -> fail("Is problem")
            null -> fail("is null response")
            is OrderResponse.Accepted -> TODO()
        }

        println(response)
    }


    @Test
    fun `if invalid customer id is passed break`() {
        val response = orderClient.deleteOrder("123")

        when {
            response.statusCode.is2xxSuccessful -> println("SUCCESS!")
            response.statusCode.isError -> fail("is else response")
        }
        println(response)
    }

}