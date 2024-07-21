package nel.marco.example.a.dto

import nel.marco.hidden.annotation.UsageMarker

@UsageMarker(
    emptyArray(),
    emptyArray(),
    emptyArray()
)
data class Order(
    val orderId: String,
    val deliveryInfo: DeliveryInfo
)
@UsageMarker(
    emptyArray(),
    emptyArray(),
    emptyArray()
)
data class DeliveryInfo(
    val orderId: String,
    val deliveryId: String?
)
@UsageMarker(
    emptyArray(),
    emptyArray(),
    emptyArray()
)
data class Customer(
    val customerId: String,
)