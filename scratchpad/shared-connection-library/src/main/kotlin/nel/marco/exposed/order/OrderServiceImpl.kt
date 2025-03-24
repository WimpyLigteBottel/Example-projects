package nel.marco.exposed.order

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import nel.marco.exposed.order.dto.DeliveryInfo
import nel.marco.exposed.order.dto.Order
import nel.marco.hidden.annotation.UsageMarker
import nel.marco.hidden.clients.OrderBasicHttpClient
import nel.marco.hidden.clients.OrderDeliveryHttpClient
import nel.marco.hidden.dto.ApplicationEnum


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

class OrderServiceImpl(
    private val orderBasicHttpClient: OrderBasicHttpClient,
    private val orderDeliveryHttpClient: OrderDeliveryHttpClient

) : OrderService {
    override suspend fun findDetailedOrder(orderId: String): Order = withContext(Dispatchers.IO) {

        //Step 1 fetch as much as possible in parallel
        val orderDeferred = async { orderBasicHttpClient.getOrder(orderId) }
        val deliveryInfoDeferred = async { orderDeliveryHttpClient.getDeliverInfo(orderId) }

        //Step 2 await all the results
        awaitAll(orderDeferred, deliveryInfoDeferred)


        //Step 3 convert to your specific domain
        val order = orderDeferred.await().convert()
        val deliveryInfo = deliveryInfoDeferred.await().convert()

        order.copy(deliveryInfo = deliveryInfo)
    }

    override suspend fun findDeliveryInfo(orderId: String): DeliveryInfo = withContext(Dispatchers.IO) {
        //Step 1 fetch as much as possible in parallel
        val deliveryInfoDeferred = async { orderDeliveryHttpClient.getDeliverInfo(orderId) }

        //Step 2 await all the results
        awaitAll(deliveryInfoDeferred)


        //Step 3 convert to your specific domain
        deliveryInfoDeferred.await().convert()
    }
}