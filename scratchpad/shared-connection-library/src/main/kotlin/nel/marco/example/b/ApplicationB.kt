package nel.marco.example.b

import nel.marco.example.b.dto.Customer
import nel.marco.example.b.dto.DeliveryInfo
import nel.marco.example.b.dto.Order
import nel.marco.exposed.customer.CustomerService
import nel.marco.exposed.order.OrderService

class ApplicationBContract(
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

    suspend fun findDeliveryInfo(orderId: String): DeliveryInfo {
        val deliveryInfo = orderService.findDeliveryInfo(orderId)

        return DeliveryInfo(
            orderId = orderId,
            deliveryId = deliveryInfo.deliveryId
        )
    }


    suspend fun findCustomer(customerId: String): Customer {

        val customer = customerService.findCustomer(customerId)

        return Customer(
            customerId = customer.id
        )
    }
}