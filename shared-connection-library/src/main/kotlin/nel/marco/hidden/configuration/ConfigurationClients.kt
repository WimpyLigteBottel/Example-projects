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
    environment: Environment
) {
    private val config: ConfigProperties = when {
        environment.matchesProfiles("pro") -> ConfigurationClientsProperties.pro
        environment.matchesProfiles("stg") -> ConfigurationClientsProperties.stg
        else -> ConfigurationClientsProperties.stg
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

object ConfigurationClientsProperties {
    val stg = ConfigProperties.ConfigurationStg(
        "http://stg.url:8080",
        "http://stg.url:8081",
        "http://stg.url:8082",
    )
    val pro = ConfigProperties.ConfigurationPro(
        "http://pro.url:8080",
        "http://pro.url:8081",
        "http://pro.url:8082",
    )
}

sealed class ConfigProperties {
    abstract val orderUrl: String
    abstract val deliveryUrl: String
    abstract val customerUrl: String

    class ConfigurationStg(
        override val orderUrl: String,
        override val deliveryUrl: String,
        override val customerUrl: String
    ) : ConfigProperties()

    class ConfigurationPro(
        override val orderUrl: String,
        override val deliveryUrl: String,
        override val customerUrl: String
    ) : ConfigProperties()
}
