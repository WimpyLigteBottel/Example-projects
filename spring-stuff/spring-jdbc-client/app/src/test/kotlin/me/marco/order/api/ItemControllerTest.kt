package me.marco.order.api

import me.marco.order.api.models.CreateOrderRequest
import me.marco.order.api.models.OrderResponse
import me.marco.order.client.OrderClient
import me.marco.order.client.OrderItemClient
import me.marco.order.dao.OrderItemDao
import me.marco.order.dao.OrderDao
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
    lateinit var orderDao: OrderDao

    @Autowired
    lateinit var orderItemJdbcClient: OrderItemDao

    @BeforeEach
    fun setUp() {
        orderItemJdbcClient.deleteItems()
        orderDao.deleteOrders()
    }

    @Test
    fun `find order with items`() {
        var order = orderClient.createOrder(CreateOrderRequest("name")).body as OrderResponse.OK
        val item1 = orderItemClient.addItemToOrder(order.order.orderId.toLong(), "item-name-1")
        val item2 = orderItemClient.addItemToOrder(order.order.orderId.toLong(), "item-name-2")
        order = orderClient.getOrder(order.order.orderId).body as OrderResponse.OK

        assertThat(order.order.items).hasSize(2)
    }

    @Test
    fun addItemToOrder() {
        val order = orderClient.createOrder(CreateOrderRequest("name")).body as OrderResponse.OK

        val item = orderItemClient.addItemToOrder(order.order.orderId.toLong(), "item-name")

        val actual = (orderClient.getOrder(order.order.orderId).body as OrderResponse.OK).order

        assertThat(actual.items).hasSize(1)
    }

    @Test
    fun deleteItem() {

        var order = orderClient.createOrder(CreateOrderRequest("name")).body as OrderResponse.OK
        val item1 = orderItemClient.addItemToOrder(order.order.orderId.toLong(), "item-name-1")
        val item2 = orderItemClient.addItemToOrder(order.order.orderId.toLong(), "item-name-2")
        order = orderClient.getOrder(order.order.orderId).body as OrderResponse.OK

        assertThat(order.order.items).hasSize(2)


        orderItemClient.deleteItem(order.order.orderId.toLong(),item1.body!!.toLong())

        order = orderClient.getOrder(order.order.orderId).body as OrderResponse.OK

        assertThat(order.order.items).hasSize(1)
    }

}