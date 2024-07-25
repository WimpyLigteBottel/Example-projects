package nel.marco.hidden.clients.customer

import org.springframework.web.reactive.function.client.WebClient


data class DetailedCustomer(val id: String)


class DetailedCustomerHttpClient(
    private val webClient: WebClient
) {

    internal fun getCustomer(customerId: String): DetailedCustomer {
        //Do http call

        return DetailedCustomer(customerId)
    }
}
