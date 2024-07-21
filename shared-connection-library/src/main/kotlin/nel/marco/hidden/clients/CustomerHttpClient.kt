package nel.marco.hidden.clients

import org.springframework.web.reactive.function.client.WebClient


data class Customer(val id: String)


class CustomerHttpClient(
    private val webClient: WebClient
) {

    internal fun getCustomer(orderId: String): Customer {
        //Do http call

        return Customer(orderId)
    }
}
