package nel.marco.hidden.configuration

import nel.marco.hidden.clients.OrderBasicHttpClient
import nel.marco.hidden.clients.OrderDeliveryHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ConfigurationClients {

    @Bean
    fun orderBasicHttpClient(webClient: WebClient): OrderBasicHttpClient {
        return OrderBasicHttpClient(webClient)
    }

    @Bean
    fun orderDeliveryHttpClient(webClient: WebClient): OrderDeliveryHttpClient {
        return OrderDeliveryHttpClient(webClient)
    }
}