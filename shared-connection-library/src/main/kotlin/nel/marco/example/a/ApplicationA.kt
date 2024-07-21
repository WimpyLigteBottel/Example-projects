package nel.marco.example.a

import nel.marco.example.a.dto.Customer
import nel.marco.example.a.dto.DeliveryInfo
import nel.marco.example.a.dto.Order
import nel.marco.exposed.customer.CustomerService
import nel.marco.exposed.order.OrderService
import nel.marco.hidden.annotation.UsageMarker

@UsageMarker(
    emptyArray(),
    emptyArray(),
    emptyArray()
)
class ApplicationAContract(
    private val orderService: OrderService,
    private val customerService: CustomerService
) {
    suspend fun findDetailedOrder(orderId: String): Order {

        val findDetailedOrder = orderService.findDetailedOrder(orderId)

        return Order(
            orderId = findDetailedOrder.orderId,
            deliveryInfo = DeliveryInfo(
                orderId = findDetailedOrder.orderId,
                deliveryId = findDetailedOrder.deliveryInfo?.deliveryId
            )
        )
    }

    suspend fun findCustomer(customerId: String): Customer {

        val customer = customerService.findCustomer(customerId)

        return Customer(
            customerId = customer.id
        )
    }
}