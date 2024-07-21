package nel.marco.exposed.order

import nel.marco.hidden.clients.BasicOrder
import nel.marco.exposed.order.dto.DeliveryInfo
import nel.marco.hidden.clients.DeliveryInfo as IntegrationDeliverInfo
import nel.marco.exposed.order.dto.Order


fun BasicOrder.convert(deliveryInfo: IntegrationDeliverInfo? = null) = Order(
    orderId = this.id,
    deliveryInfo = deliveryInfo?.convert()
)

fun IntegrationDeliverInfo.convert() = DeliveryInfo(
    deliveryId = this.id,
)