package nel.marco.hidden.clients

import org.springframework.web.reactive.function.client.WebClient

data class BasicOrder(val id: String)

class OrderBasicHttpClient(
    private val webClient: WebClient
) {


    fun getOrder(orderId: String): BasicOrder {
        //Do http call

        return BasicOrder(orderId)
    }
}