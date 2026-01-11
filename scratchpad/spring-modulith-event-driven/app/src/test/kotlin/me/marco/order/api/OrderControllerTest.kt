package me.marco.order.api

import me.marco.customer.api.CustomerClient
import me.marco.customer.api.models.CreateCustomerRequest
import me.marco.customer.api.models.CustomerResponse
import me.marco.order.api.models.AddItemRequest
import me.marco.order.api.models.CreateOrderRequest
import me.marco.order.api.models.MarkAsPaidRequest
import me.marco.order.api.models.OrderResponse
import me.marco.order.service.dto.OrderItem
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatusCode

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
)
class OrderControllerTest {
    @Autowired
    lateinit var orderClient: OrderClient

    @Autowired
    lateinit var customerClient: CustomerClient

    fun createDefaultCustomer(): String {
        val customer = customerClient.createCustomer(CreateCustomerRequest("name", 0, "email@email.com")).body as? CustomerResponse.OK

        customer ?: throw RuntimeException("Faile")
        return customer.id
    }

    @Test
    fun `if invalid customer id is passed break`() {
        val response = orderClient.createOrder(CreateOrderRequest("zxc"))

        assertThat(response.statusCode).isEqualTo(HttpStatusCode.valueOf(400))
    }

    @Test
    fun `when order is old then expect that they get cancelled`() {
        val customerId = createDefaultCustomer()
        val responseA = orderClient.createOrder(CreateOrderRequest(customerId)).body as? OrderResponse.OK
        val responseB = orderClient.createOrder(CreateOrderRequest(customerId)).body as? OrderResponse.OK
        val responseC = orderClient.createOrder(CreateOrderRequest(customerId)).body as? OrderResponse.OK
        Thread.sleep(2000)

        orderClient.getOrder(responseA!!.orderId).also {
            assertThat(it.statusCode.value()).isEqualTo(400)
        }
        orderClient.getOrder(responseB!!.orderId).also {
            assertThat(it.statusCode.value()).isEqualTo(400)
        }
        orderClient.getOrder(responseC!!.orderId).also {
            assertThat(it.statusCode.value()).isEqualTo(400)
        }
    }


    @Test
    fun `when customer is gone , expect all customer orders to be deleted`() {
        val customerId = createDefaultCustomer()
        val responseA = orderClient.createOrder(CreateOrderRequest(customerId)).body as? OrderResponse.OK
        val responseB = orderClient.createOrder(CreateOrderRequest(customerId)).body as? OrderResponse.OK
        val responseC = orderClient.createOrder(CreateOrderRequest(customerId)).body as? OrderResponse.OK

        assertThat(responseA?.orderId).isNotEmpty()
        assertThat(responseB?.orderId).isNotEmpty()
        assertThat(responseC?.orderId).isNotEmpty()

        customerClient.deleteCustomer(customerId)

        // Due to how the delete works
        Thread.sleep(2000)

        orderClient.getOrder(responseA!!.orderId).also {
            assertThat(it.statusCode.value()).isEqualTo(400)
        }
        orderClient.getOrder(responseB!!.orderId).also {
            assertThat(it.statusCode.value()).isEqualTo(400)
        }
        orderClient.getOrder(responseC!!.orderId).also {
            assertThat(it.statusCode.value()).isEqualTo(400)
        }
    }


    @Test
    fun `able to create an order`() {
        val customerId = createDefaultCustomer()
        val response = orderClient.createOrder(CreateOrderRequest(customerId)).body as? OrderResponse.OK

        Assertions.assertThat(response?.orderId).isNotEmpty()
        Assertions.assertThat(response?.totalAmount).isEqualTo(0.0)
        Assertions.assertThat(response?.isPaid).isEqualTo(false)
        Assertions.assertThat(response?.version).isEqualTo(1)
        Assertions.assertThat(response?.items).isEqualTo(emptyList<OrderItem>())
    }

    @Test
    fun `order created gets closed after x period`() {
        val response = orderClient.createOrder(CreateOrderRequest(createDefaultCustomer())).body as? OrderResponse.OK

        Thread.sleep(2000)

        val order = orderClient.getOrder(response!!.orderId)

        Assertions.assertThat(order.statusCode.value()).isEqualTo(400)
    }


    @Test
    fun `when deleting order that is already deleted should be okay`() {
        val response = orderClient.createOrder(CreateOrderRequest(createDefaultCustomer())).body as? OrderResponse.OK
        val x = orderClient.deleteOrder(response!!.orderId)
        Assertions.assertThat(x.statusCode.is2xxSuccessful).isTrue()
        val order = orderClient.getOrder(response.orderId)
        Assertions.assertThat(order.statusCode.value()).isEqualTo(400)
    }

    @Test
    fun `able to create and add item to an order`() {
        val orderId = orderClient.createOrder(CreateOrderRequest(createDefaultCustomer())).body!! as? OrderResponse.OK
        val response =
            orderClient.addItem(
                orderId!!.orderId,
                AddItemRequest(
                    itemId = "123",
                    name = "test",
                    price = 123.00,
                    quantity = 1,
                ),
            ).body as? OrderResponse.OK

        Assertions.assertThat(response?.orderId).isEqualTo(orderId.orderId)
        Assertions.assertThat(response?.totalAmount).isEqualTo(123.0)
        Assertions.assertThat(response?.isPaid).isEqualTo(false)
        Assertions.assertThat(response?.version).isEqualTo(2)

        val actual = response?.items?.first()

        Assertions.assertThat(actual?.itemId).isEqualTo("123")
        Assertions.assertThat(actual?.name).isEqualTo("test")
        Assertions.assertThat(actual?.price).isEqualTo(123.0)
        Assertions.assertThat(actual?.quantity).isEqualTo(1)
    }

    @Test
    fun `create an order and pay for it`() {
        val response = orderClient.createOrder(CreateOrderRequest(createDefaultCustomer())).body as? OrderResponse.OK
        val orderId = response!!.orderId
        orderClient.addItem(orderId, AddItemRequest(itemId = orderId, name = "test", price = 123.00, quantity = 1))
        orderClient.markAsPaid(orderId, MarkAsPaidRequest("Credit card"))
        val order = orderClient.getOrder(orderId).body as OrderResponse.OK

        Assertions.assertThat(order.isPaid).isTrue()
    }

    @Test
    fun `when it takes too long to pay for order then items will be removed`() {
        val r = orderClient.createOrder(CreateOrderRequest(createDefaultCustomer())).body as? OrderResponse.OK
        val orderId = r?.orderId!!
        orderClient.addItem(orderId, AddItemRequest(itemId = orderId, name = "test", price = 123.00, quantity = 1))

        //  Hacky way to remove item
        Thread.sleep(500)

        val order = orderClient.getOrder(orderId).body as OrderResponse.OK

        assertThat(order?.items).isEmpty()
    }
}