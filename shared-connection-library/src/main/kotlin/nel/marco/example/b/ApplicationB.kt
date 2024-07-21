package nel.marco.example.b

import nel.marco.example.b.dto.DeliveryInfo
import nel.marco.example.b.dto.Order
import nel.marco.hidden.clients.BasicOrder
import nel.marco.hidden.clients.OrderBasicHttpClient
import nel.marco.internal.service.OrderService
import org.springframework.web.reactive.function.client.WebClient
//import nel.marco.hidden.clients.DeliveryInfo as HiddenDeliveryInfo

class ApplicationBContract(
    private val orderService: OrderService,
) {
    fun findDetailedOrder(orderId: String): Order {

        val httpClientOrder = BasicOrder(orderId)
        val deliverInfo = nel.marco.hidden.clients.DeliveryInfo("")
        val customHttpClient = OrderBasicHttpClient(WebClient.create())

        val findDetailedOrder = orderService.findDetailedOrder(orderId)

        return Order(
            orderId = findDetailedOrder.orderId,
            deliveryInfo = DeliveryInfo(
                orderId = findDetailedOrder.orderId,
                deliveryId = findDetailedOrder.deliveryInfo.deliveryId
            )
        )
    }

    fun findDeliveryInfo(orderId: String): DeliveryInfo {
        val deliveryInfo = orderService.findDeliveryInfo(orderId)

        return DeliveryInfo(
            orderId = deliveryInfo.orderId,
            deliveryId = deliveryInfo.deliveryId
        )
    }
}