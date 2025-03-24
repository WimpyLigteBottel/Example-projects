package nel.marco

import nel.marco.hidden.configuration.ConfigProperties
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.stereotype.Component

@SpringBootApplication
open class Application {
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}


@Component
open class LogConfigurationClientProperties(
    private val configProperties: ConfigProperties
) : CommandLineRunner {
    override fun run(vararg args: String?) {

        val text = """
        Your clients is using the following properties
        =============
        orderUrl = ${configProperties.orderUrl}
        customerUrl = ${configProperties.customerUrl}
        deliveryUrl = ${configProperties.deliveryUrl}
        =============
        """.trimIndent()

        println(text)
    }
}