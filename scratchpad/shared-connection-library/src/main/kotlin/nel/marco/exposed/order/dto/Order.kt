package nel.marco.exposed.order.dto

data class Order(
    val orderId: String,
    val deliveryInfo: DeliveryInfo?
)