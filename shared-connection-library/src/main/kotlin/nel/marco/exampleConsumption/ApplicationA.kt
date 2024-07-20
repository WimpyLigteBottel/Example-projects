package nel.marco.exampleConsumption

import nel.marco.internal.OrderService

data class OrderApplicationA(
    val orderId: String,
    val deliveryInfo: DeliveryInfoApplicationA
)

data class DeliveryInfoApplicationA(
    val orderId: String,
    val deliveryId: String
)


class ApplicationAContract(
    private val orderService: OrderService,
) {
    fun findDetailedOrder(orderId: String): OrderApplicationA {

        val findDetailedOrder = orderService.findDetailedOrder(orderId)

        return OrderApplicationA(
            orderId = findDetailedOrder.orderId,
            deliveryInfo = DeliveryInfoApplicationA(
                orderId = findDetailedOrder.orderId,
                deliveryId = findDetailedOrder.deliveryInfo.deliveryId
            )
        )
    }
}