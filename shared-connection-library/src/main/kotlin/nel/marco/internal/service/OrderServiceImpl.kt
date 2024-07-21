package nel.marco.internal.service

import nel.marco.internal.dto.DeliveryInfo
import nel.marco.internal.dto.Order
import nel.marco.hidden.clients.OrderBasicHttpClient
import nel.marco.hidden.clients.OrderDeliveryHttpClient

class OrderServiceImpl(
    private val orderBasicHttpClient: OrderBasicHttpClient,
    private val orderDeliveryHttpClient: OrderDeliveryHttpClient

) : OrderService {
    override fun findDetailedOrder(orderId: String): Order {

        val order = orderBasicHttpClient.getOrder(orderId)
        val deliverInfo = orderDeliveryHttpClient.getDeliverInfo(orderId)

        return Order(
            orderId = order.id,
            deliveryInfo = DeliveryInfo(order.id, deliverInfo.id)
        )
    }

    override fun findDeliveryInfo(orderId: String): DeliveryInfo {

        val deliverInfo = orderDeliveryHttpClient.getDeliverInfo(orderId)

        return DeliveryInfo(
            orderId = orderId,
            deliveryId = deliverInfo.id
        )
    }
}