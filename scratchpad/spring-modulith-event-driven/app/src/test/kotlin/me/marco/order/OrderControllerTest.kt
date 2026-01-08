package me.marco.order

import me.marco.common.OrderItem
import me.marco.order.api.AddItemRequest
import me.marco.order.api.CreateOrderRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class OrderControllerTest {

    @Autowired
    lateinit var orderClient: OrderClient

    @Test
    fun createOrder() {
        val response = orderClient.createOrder(CreateOrderRequest())

        assertThat(response.body?.orderId).isNotEmpty()
        assertThat(response.body?.totalAmount).isEqualTo(0.0)
        assertThat(response.body?.isPaid).isEqualTo(false)
        assertThat(response.body?.version).isEqualTo(1)
        assertThat(response.body?.error).isEqualTo(null)
        assertThat(response.body?.items).isEqualTo(emptyList<OrderItem>())
    }

    @Test
    fun addItem() {
        orderClient.createOrder(CreateOrderRequest("123"))
        val response = orderClient.addItem(
            "123", AddItemRequest(
                itemId = "123",
                name = "test",
                price = 123.00,
                quantity = 1
            )
        )

        assertThat(response.body?.orderId).isEqualTo("123")
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
    fun markAsPaid() {
    }

    @Test
    fun getOrder() {
    }

}