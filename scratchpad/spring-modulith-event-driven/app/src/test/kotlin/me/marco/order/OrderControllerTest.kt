package me.marco.order

import me.marco.common.OrderItem
import me.marco.order.api.AddItemRequest
import me.marco.order.api.CreateOrderRequest
import me.marco.order.api.MarkAsPaidRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class OrderControllerTest {

    @Autowired
    lateinit var orderClient: OrderClient

    @Test
    fun `able to create an order`() {
        val response = orderClient.createOrder(CreateOrderRequest())

        assertThat(response.body?.orderId).isNotEmpty()
        assertThat(response.body?.totalAmount).isEqualTo(0.0)
        assertThat(response.body?.isPaid).isEqualTo(false)
        assertThat(response.body?.version).isEqualTo(1)
        assertThat(response.body?.error).isEqualTo(null)
        assertThat(response.body?.items).isEqualTo(emptyList<OrderItem>())
    }

    @Test
    fun `able to create and add item to an order`() {
        val orderId = UUID.randomUUID().toString()
        orderClient.createOrder(CreateOrderRequest(orderId)).body
        val response = orderClient.addItem(
            orderId, AddItemRequest(
                itemId = "123", name = "test", price = 123.00, quantity = 1
            )
        )

        assertThat(response.body?.orderId).isEqualTo(orderId)
        assertThat(response.body?.totalAmount).isEqualTo(123.0)
        assertThat(response.body?.isPaid).isEqualTo(false)
        assertThat(response.body?.version).isEqualTo(2)
        assertThat(response.body?.error).isEqualTo(null)

        val actual = response.body?.items?.first()

        assertThat(actual?.itemId).isEqualTo("123")
        assertThat(actual?.name).isEqualTo("test")
        assertThat(actual?.price).isEqualTo(123.0)
        assertThat(actual?.quantity).isEqualTo(1)
    }

    @Test
    fun `create an order and pay for it`() {
        val orderId = UUID.randomUUID().toString()
        orderClient.createOrder(CreateOrderRequest(orderId))
        orderClient.addItem(orderId, AddItemRequest(itemId = orderId, name = "test", price = 123.00, quantity = 1))
        orderClient.markAsPaid(orderId, MarkAsPaidRequest("Credit card"))
        val order = orderClient.getOrder(orderId).body

        assertThat(order?.isPaid).isTrue()
    }

    @Test
    fun `when it takes too long to pay for order then items will be removed`() {
        val orderId = UUID.randomUUID().toString()
        orderClient.createOrder(CreateOrderRequest(orderId))
        orderClient.addItem(orderId, AddItemRequest(itemId = orderId, name = "test", price = 123.00, quantity = 1))

        /// Hacky way to remove item
        Thread.sleep(1000)

        val order = orderClient.getOrder(orderId).body

        assertThat(order?.items).isEmpty()
    }

}