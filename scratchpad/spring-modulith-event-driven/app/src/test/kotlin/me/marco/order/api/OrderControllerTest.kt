package me.marco.order.api

import me.marco.customer.api.CustomerClient
import me.marco.customer.api.models.CreateCustomerRequest
import me.marco.order.api.models.AddItemRequest
import me.marco.order.api.models.CreateOrderRequest
import me.marco.order.api.models.MarkAsPaidRequest
import me.marco.order.service.dto.OrderItem
import org.assertj.core.api.Assertions
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
    lateinit var customerClient: CustomerClient

    fun createDefaultCustomer(): String {
        val customer = customerClient.createCustomer(CreateCustomerRequest("name", 0, "email@email.com"))

        return customer.body!!.id
    }

    @Test
    fun `able to create an order`() {
        val customerId = createDefaultCustomer()
        val response = orderClient.createOrder(CreateOrderRequest(customerId))

        Assertions.assertThat(response.body?.orderId).isNotEmpty()
        Assertions.assertThat(response.body?.totalAmount).isEqualTo(0.0)
        Assertions.assertThat(response.body?.isPaid).isEqualTo(false)
        Assertions.assertThat(response.body?.version).isEqualTo(1)
        Assertions.assertThat(response.body?.error).isEqualTo(null)
        Assertions.assertThat(response.body?.items).isEqualTo(emptyList<OrderItem>())
    }

    @Test
    fun `order created gets closed after x period`() {
        val response = orderClient.createOrder(CreateOrderRequest(createDefaultCustomer())).body!!

        Thread.sleep(1000)

        val order = orderClient.getOrder(response.orderId)

        Assertions.assertThat(order.statusCode.value()).isEqualTo(404)
    }


    @Test
    fun `when deleting order that is already deleted should be okay`() {
        val response = orderClient.createOrder(CreateOrderRequest(createDefaultCustomer())).body!!
        val x = orderClient.deleteOrder(response.orderId)
        Assertions.assertThat(x.statusCode.is2xxSuccessful).isTrue()
        val order = orderClient.getOrder(response.orderId)
        Assertions.assertThat(order.statusCode.value()).isEqualTo(404)
    }

    @Test
    fun `able to create and add item to an order`() {
        val orderId = orderClient.createOrder(CreateOrderRequest(createDefaultCustomer())).body!!.orderId
        val response =
            orderClient.addItem(
                orderId,
                AddItemRequest(
                    itemId = "123",
                    name = "test",
                    price = 123.00,
                    quantity = 1,
                ),
            )

        Assertions.assertThat(response.body?.orderId).isEqualTo(orderId)
        Assertions.assertThat(response.body?.totalAmount).isEqualTo(123.0)
        Assertions.assertThat(response.body?.isPaid).isEqualTo(false)
        Assertions.assertThat(response.body?.version).isEqualTo(2)
        Assertions.assertThat(response.body?.error).isEqualTo(null)

        val actual = response.body?.items?.first()

        Assertions.assertThat(actual?.itemId).isEqualTo("123")
        Assertions.assertThat(actual?.name).isEqualTo("test")
        Assertions.assertThat(actual?.price).isEqualTo(123.0)
        Assertions.assertThat(actual?.quantity).isEqualTo(1)
    }

    @Test
    fun `create an order and pay for it`() {
        val orderId = orderClient.createOrder(CreateOrderRequest(createDefaultCustomer())).body!!.orderId
        orderClient.addItem(orderId, AddItemRequest(itemId = orderId, name = "test", price = 123.00, quantity = 1))
        orderClient.markAsPaid(orderId, MarkAsPaidRequest("Credit card"))
        val order = orderClient.getOrder(orderId).body

        Assertions.assertThat(order?.isPaid).isTrue()
    }

    @Test
    fun `when it takes too long to pay for order then items will be removed`() {
        val orderId = orderClient.createOrder(CreateOrderRequest(createDefaultCustomer())).body!!.orderId
        orderClient.addItem(orderId, AddItemRequest(itemId = orderId, name = "test", price = 123.00, quantity = 1))

        //  Hacky way to remove item
        Thread.sleep(500)

        val order = orderClient.getOrder(orderId).body

        Assertions.assertThat(order?.items).isEmpty()
    }
}