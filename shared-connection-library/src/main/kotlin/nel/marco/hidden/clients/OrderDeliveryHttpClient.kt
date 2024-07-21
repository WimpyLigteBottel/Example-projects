package nel.marco.hidden.clients

import org.springframework.web.reactive.function.client.WebClient


data class DeliveryInfo(val id: String)


class OrderDeliveryHttpClient(
    private val webClient: WebClient
) {

    internal fun getDeliverInfo(orderId: String): DeliveryInfo {
        //Do http call

        return DeliveryInfo(orderId)
    }
}
