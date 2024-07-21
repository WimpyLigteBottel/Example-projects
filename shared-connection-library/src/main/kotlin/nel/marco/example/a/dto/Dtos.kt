package nel.marco.example.a.dto


data class OrderApplicationA(
    val orderId: String,
    val deliveryInfo: DeliveryInfoApplicationA
)

data class DeliveryInfoApplicationA(
    val orderId: String,
    val deliveryId: String
)