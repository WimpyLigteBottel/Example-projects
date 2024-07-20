package nel.marco.internal

import nel.marco.clients.OrderBasicHttpClient
import nel.marco.clients.OrderDeliveryHttpClient
import nel.marco.internal.annotation.UsageMarker
import nel.marco.internal.dto.ApplicationEnum
import nel.marco.internal.dto.DeliveryInfo
import nel.marco.internal.dto.Order

interface OrderService {

    @UsageMarker(
        applications = [ApplicationEnum.ApplicationA, ApplicationEnum.ApplicationB],
        consumingServices = [OrderBasicHttpClient::class, OrderDeliveryHttpClient::class],
        whatDoesItSolve = [
            "see if the order exists",
            "find out what happened to delivery"
        ]
    )
    fun findDetailedOrder(orderId: String): Order

    @UsageMarker(
        applications = [ApplicationEnum.ApplicationB],
        consumingServices = [OrderDeliveryHttpClient::class],
        whatDoesItSolve = ["find out what happened to delivery"]
    )
    fun findDeliveryInfo(orderId: String): DeliveryInfo
}

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