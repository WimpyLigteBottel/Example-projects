package nel.marco.hidden.configuration

import nel.marco.hidden.clients.CustomerHttpClient
import nel.marco.hidden.clients.OrderBasicHttpClient
import nel.marco.hidden.clients.OrderDeliveryHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ConfigurationClient(
    configurationClientsProperties: ConfigurationClientsProperties,
    environment: Environment
) {
    private val config: ConfigProperties = when {
        environment.matchesProfiles("pro") -> configurationClientsProperties.pro
        environment.matchesProfiles("stg") -> configurationClientsProperties.stg
        else -> configurationClientsProperties.stg
    }

    @Bean
    fun orderBasicHttpClient(webClient: WebClient.Builder) =
        OrderBasicHttpClient(webClient.baseUrl(config.orderUrl).build())

    @Bean
    fun orderDeliveryHttpClient(webClient: WebClient.Builder) =
        OrderDeliveryHttpClient(webClient.baseUrl(config.deliveryUrl).build())

    @Bean
    fun customerHttpClient(webClient: WebClient.Builder) =
        CustomerHttpClient(webClient.baseUrl(config.customerUrl).build())
}

@Configuration
class ConfigurationClientsProperties {
    lateinit var stg: ConfigProperties.ConfigurationStg
    lateinit var pro: ConfigProperties.ConfigurationPro
}

sealed class ConfigProperties(
    val orderUrl: String,
    val deliveryUrl: String,
    val customerUrl: String
) {
    class ConfigurationStg : ConfigProperties(
        orderUrl = "http://stg.url:8080",
        deliveryUrl = "http://stg.url:8081",
        customerUrl = "http://stg.url:8082",
    )

    class ConfigurationPro : ConfigProperties(
        orderUrl = "http://pro.url:8080",
        deliveryUrl = "http://pro.url:8081",
        customerUrl = "http://pro.url:8082",
    )
}
