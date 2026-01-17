package me.marco.order.api

import me.marco.order.api.models.CreateOrderRequest
import me.marco.order.api.models.OrderResponse
import me.marco.order.client.OrderClient
import me.marco.order.dao.OrderItemDao
import me.marco.order.dao.OrderDao
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
)
class OrderControllerTest {
    @Autowired
    lateinit var orderClient: OrderClient

    @Autowired
    lateinit var orderDao: OrderDao
    @Autowired
    lateinit var orderItemClient: OrderItemDao

    @BeforeEach
    fun setUp() {
        orderItemClient.deleteItems()
        orderDao.deleteOrders()
    }

    @Test
    fun `creating an order`() {
        val response = orderClient.createOrder(CreateOrderRequest("zxc"))

        when (response.body) {
            is OrderResponse.OK -> {
                println(response.body)
            }

            else -> fail("Is problem")
        }

        println(response)
    }


    @Test
    fun `creating an 100's orders`() {

        var ids = mutableListOf<String>()
        repeat(100) {
            val response = orderClient.createOrder(CreateOrderRequest("zxc"))
            when (val body = response.body) {
                is OrderResponse.OK -> {
                    ids.add(body.order.orderId)
                }

                else -> fail("Is problem")
            }
        }

        var orders = orderClient.getOrders(ids).body!!

        println(orders.size)
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