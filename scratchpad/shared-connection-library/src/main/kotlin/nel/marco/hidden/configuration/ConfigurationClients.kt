package nel.marco.hidden.configuration

import nel.marco.hidden.clients.OrderBasicHttpClient
import nel.marco.hidden.clients.OrderDeliveryHttpClient
import nel.marco.hidden.clients.customer.DetailedCustomerHttpClient
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.web.reactive.function.client.WebClient


/**
 * This will need to be 'extended' to make sure its on the right path and all the clients are being created
 *
 * Remember to @Configuration it to get the clients
 */
abstract class ConfigurationClients {

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
    open fun detailedCustomerHttpClient(webClient: WebClient.Builder, config: ConfigProperties) =
        DetailedCustomerHttpClient(webClient.baseUrl(config.customerUrl).build())
}

/**
 * This is code config because having this as application.yml properties won't work
 * and makes life much more difficult
 */
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

/**
 * You can extend this class to have a nice logger on startup to see what links you have active.
 *
 * Need to add @component to get the run on startup
 */
abstract class LogConfigurationClientProperties(
    private val configProperties: ConfigProperties
) : CommandLineRunner {

    val log = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String?) {
        log.info(configProperties.getConfigText())
    }
}

class ConfigProperties(
    val orderUrl: String,
    val deliveryUrl: String,
    val customerUrl: String
) {

    fun getConfigText(): String {
        return """
        Your clients is using the following properties
        =============
        orderUrl = $orderUrl
        customerUrl = $customerUrl
        deliveryUrl = $deliveryUrl
        =============
        """.trimIndent()
    }
}
