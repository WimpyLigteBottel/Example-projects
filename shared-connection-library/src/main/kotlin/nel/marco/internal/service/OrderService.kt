package nel.marco.internal.service

import nel.marco.internal.annotation.UsageMarker
import nel.marco.internal.dto.ApplicationEnum
import nel.marco.internal.dto.DeliveryInfo
import nel.marco.internal.dto.Order
import nel.marco.hidden.clients.OrderBasicHttpClient
import nel.marco.hidden.clients.OrderDeliveryHttpClient

interface OrderService {

    @UsageMarker(
        applications = [ApplicationEnum.ApplicationA, ApplicationEnum.ApplicationB],
        consumingServices = [OrderBasicHttpClient::class, OrderDeliveryHttpClient::class],
        businessProblemBeingSolved = [
            "see if the order exists",
            "find out what happened to delivery"
        ]
    )
    fun findDetailedOrder(orderId: String): Order

    @UsageMarker(
        applications = [ApplicationEnum.ApplicationB],
        consumingServices = [OrderDeliveryHttpClient::class],
        businessProblemBeingSolved = ["find out what happened to delivery"]
    )
    fun findDeliveryInfo(orderId: String): DeliveryInfo
}

