package nel.marco.clients

class OrderBasicHttpClient {

    data class BasicOrder(val id: String)

    fun getOrder(orderId: String): BasicOrder {
        //Do http call

        return BasicOrder(orderId)
    }
}