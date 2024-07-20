package nel.marco.clients

class OrderDeliveryHttpClient {

    data class DeliveryInfo(val id: String)

    fun getDeliverInfo(orderId: String): DeliveryInfo {
        //Do http call

        return DeliveryInfo(orderId)
    }
}