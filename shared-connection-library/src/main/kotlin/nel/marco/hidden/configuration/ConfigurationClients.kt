package nel.marco.hidden.configuration

import nel.marco.hidden.clients.CustomerHttpClient
import nel.marco.hidden.clients.OrderBasicHttpClient
import nel.marco.hidden.clients.OrderDeliveryHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.reactive.function.client.WebClient

@Configuration
open class ConfigurationClients {

    @Bean
    open fun configProperties(environment: Environment): ConfigProperties {
        val configProperties = when {
            environment.matchesProfiles("pro") -> ConfigurationClientsProperties.pro
            environment.matchesProfiles("stg") -> ConfigurationClientsProperties.stg
            else -> ConfigurationClientsProperties.test
        }
        return configProperties
    }

    @Bean
    open fun orderBasicHttpClient(webClient: WebClient.Builder, config: ConfigProperties) =
        OrderBasicHttpClient(webClient.baseUrl(config.orderUrl).build())

    @Bean
    open fun orderDeliveryHttpClient(webClient: WebClient.Builder, config: ConfigProperties) =
        OrderDeliveryHttpClient(webClient.baseUrl(config.deliveryUrl).build())

    @Bean
    open fun customerHttpClient(webClient: WebClient.Builder, config: ConfigProperties) =
        CustomerHttpClient(webClient.baseUrl(config.customerUrl).build())
}

object ConfigurationClientsProperties {
    val stg = ConfigProperties(
        orderUrl = "http://stg.url:8080",
        deliveryUrl = "http://stg.url:8081",
        customerUrl = "http://stg.url:8082",
    )
    val pro = ConfigProperties(
        orderUrl = "http://pro.url:8080",
        deliveryUrl = "http://pro.url:8081",
        customerUrl = "http://pro.url:8082",
    )
    val test = ConfigProperties(
        orderUrl = "http://test.url:8080",
        deliveryUrl = "http://test.url:8081",
        customerUrl = "http://test.url:8082",
    )
}

class ConfigProperties(
    val orderUrl: String,
    val deliveryUrl: String,
    val customerUrl: String
)
