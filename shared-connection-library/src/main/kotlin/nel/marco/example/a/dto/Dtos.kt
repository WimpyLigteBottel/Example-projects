package nel.marco.example.a.dto

data class Order(
    val orderId: String,
    val deliveryInfo: DeliveryInfo
)

data class DeliveryInfo(
    val orderId: String,
    val deliveryId: String?
)

data class Customer(
    val customerId: String,
)