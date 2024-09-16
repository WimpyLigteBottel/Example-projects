package marco

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.time.OffsetDateTime
import java.util.*


@SpringBootApplication
class ClientLauncher

fun main(args: Array<String>) {
    runApplication<ClientLauncher>(*args)
}


@Component
class ClientCode(
    @Value("\${telemetry.server.url}") private val serverUri: String
) : CommandLineRunner {

    private val client = WebClient
        .builder()
        .baseUrl(serverUri)
        .build()
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String?) {
        val toBodilessEntity = client
            .get()
            .uri("/") { uri ->
                val map = linkedMapOf<String,Any>(
                    "version" to "1.0.0",
                    "config" to "<custom>",
                    "application-name" to "<application-name>",
                    "offsetDateTime" to OffsetDateTime.now().toString(),
                )
                uri.path("${UUID.randomUUID()}")
                uri.queryParam("version", "{version}")
                uri.queryParam("config", "{config}")
                uri.queryParam("application-name", "{application-name}")
                uri.queryParam("offsetDateTime", "{offsetDateTime}")
                uri.build(map)
            }
            .retrieve()
            .toEntity(String::class.java)
            .block()

        log.info("status = ${toBodilessEntity?.statusCode}")
        log.info("body = ${toBodilessEntity?.body}")

        log.info("Going to shutdown now!")
        System.exit(0)
    }

}