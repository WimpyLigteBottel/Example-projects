package me.marco.order.api

import me.marco.order.dao.OrderItemJdbcClient
import me.marco.order.dao.OrderJdbcClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
)
class ItemControllerTest {

    @Autowired
    lateinit var orderItemClient: OrderItemClient

    @Autowired
    lateinit var orderClient: OrderClient

    @Autowired
    lateinit var orderJdbcClient: OrderJdbcClient

    @Autowired
    lateinit var orderItemJdbcClient: OrderItemJdbcClient

    @BeforeEach
    fun setUp() {
        orderItemJdbcClient.deleteItems()
        orderJdbcClient.deleteOrders()
    }

    @Test
    fun `find order with items`() {
        var order = orderClient.createOrder(CreateOrderRequest("name")).body as OrderResponse.OK
        val item1 = orderItemClient.addItemToOrder(order.orderId.toLong(), "item-name-1")
        val item2 = orderItemClient.addItemToOrder(order.orderId.toLong(), "item-name-2")
        order = orderClient.getOrder(order.orderId).body as OrderResponse.OK

        assertThat(order.items).hasSize(2)
    }

    @Test
    fun addItemToOrder() {
        val order = orderClient.createOrder(CreateOrderRequest("name")).body as OrderResponse.OK

        val item = orderItemClient.addItemToOrder(order.orderId.toLong(), "item-name")

        val actual = item.body as OrderResponse.OK
        assertThat(actual.items).hasSize(1)
    }

    @Test
    fun deleteItem() {

        var order = orderClient.createOrder(CreateOrderRequest("name")).body as OrderResponse.OK
        val item1 = orderItemClient.addItemToOrder(order.orderId.toLong(), "item-name-1")
        val item2 = orderItemClient.addItemToOrder(order.orderId.toLong(), "item-name-2")
        order = orderClient.getOrder(order.orderId).body as OrderResponse.OK

        assertThat(order.items).hasSize(2)


        orderItemClient.deleteItem(order.orderId.toLong(),item1.body!!.toLong())

        order = orderClient.getOrder(order.orderId).body as OrderResponse.OK

        assertThat(order.items).hasSize(1)
    }

}