package nel.marco.exampleConsumption

import nel.marco.internal.OrderService

data class OrderApplicationB(
    val orderId: String,
    val deliveryInfo: DeliveryInfoApplicationB
)

data class DeliveryInfoApplicationB(
    val orderId: String,
    val deliveryId: String
)


class ApplicationBContract(
    private val orderService: OrderService,
) {
    fun findDetailedOrder(orderId: String): OrderApplicationB {

        val findDetailedOrder = orderService.findDetailedOrder(orderId)

        return OrderApplicationB(
            orderId = findDetailedOrder.orderId,
            deliveryInfo = DeliveryInfoApplicationB(
                orderId = findDetailedOrder.orderId,
                deliveryId = findDetailedOrder.deliveryInfo.deliveryId
            )
        )
    }

    fun findDeliveryInfo(orderId: String): DeliveryInfoApplicationB {
        val deliveryInfo = orderService.findDeliveryInfo(orderId)

        return DeliveryInfoApplicationB(
            orderId = deliveryInfo.orderId,
            deliveryId = deliveryInfo.deliveryId
        )
    }
}