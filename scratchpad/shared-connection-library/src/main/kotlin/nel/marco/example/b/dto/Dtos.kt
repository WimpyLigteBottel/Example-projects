package nel.marco.example.b.dto

data class Order(
    val orderId: String,
    val deliveryInfo: DeliveryInfo
)

data class DeliveryInfo(
    val orderId: String,
    val deliveryId: String? = null
)

data class Customer(
    val customerId: String,
)