package nel.marco.example.a

import nel.marco.example.a.dto.DeliveryInfoApplicationA
import nel.marco.example.a.dto.OrderApplicationA
import nel.marco.internal.service.OrderService

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