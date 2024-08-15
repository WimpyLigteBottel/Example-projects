package marco

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import java.time.OffsetDateTime
import java.time.ZoneOffset


@SpringBootApplication
class ClientLauncher

fun main(args: Array<String>) {
    runApplication<ClientLauncher>(*args)
}


@Component
class ClientCode(
    @Value("\${telemetry.server.url}") private val serverUri: String
): CommandLineRunner {

    private val client = WebClient.builder().clientConnector(ReactorClientHttpConnector()).baseUrl(serverUri).build()
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String?) {
        val toBodilessEntity = client
            .get()
            .uri { builder ->
                builder
                    .path("/${OffsetDateTime.now()}")
                    .queryParam("version", "1.0.0")
                    .queryParam("config", "<custom>")
                    .queryParam("application-name", "ABC")
                    .queryParam("offsetDateTime", OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC))
                    .build()
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