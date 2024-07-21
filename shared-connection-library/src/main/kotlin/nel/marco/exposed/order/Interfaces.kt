package nel.marco.exposed.order

import nel.marco.hidden.annotation.UsageMarker
import nel.marco.hidden.dto.ApplicationEnum
import nel.marco.exposed.order.dto.Order
import nel.marco.hidden.clients.OrderBasicHttpClient
import nel.marco.hidden.clients.OrderDeliveryHttpClient
import nel.marco.exposed.order.dto.DeliveryInfo

interface OrderService {

    @UsageMarker(
        applications = [ApplicationEnum.ApplicationA, ApplicationEnum.ApplicationB],
        consumingServices = [OrderBasicHttpClient::class, OrderDeliveryHttpClient::class],
        businessProblemBeingSolved = [
            "see if the order exists",
            "find out what happened to delivery"
        ]
    )
    suspend fun findDetailedOrder(orderId: String): Order

    @UsageMarker(
        applications = [ApplicationEnum.ApplicationB],
        consumingServices = [OrderDeliveryHttpClient::class],
        businessProblemBeingSolved = ["find out what happened to delivery"]
    )
    suspend fun findDeliveryInfo(orderId: String): DeliveryInfo

}

