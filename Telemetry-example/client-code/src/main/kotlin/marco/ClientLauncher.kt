package marco

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.DefaultUriBuilderFactory
import java.net.URI
import java.net.URLEncoder
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
        .uriBuilderFactory(DefaultUriBuilderFactory(serverUri).apply {
            encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE
        })
        .baseUrl(serverUri)
        .build()
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String?) {
        val toBodilessEntity = client
            .get()
            .uri("/") { uri ->
                uri.path("${UUID.randomUUID()}")
                uri.queryParam("version", "1.0.0".encode())
                uri.queryParam("config", "<custom>".encode())
                uri.queryParam("application-name", "ABC")
                uri.queryParam(
                    "offsetDateTime",
                    OffsetDateTime.now().toString().encode()
                )
                URI(uri.build().toString())
            }
            .retrieve()
            .toEntity(String::class.java)
            .block()

        log.info("status = ${toBodilessEntity?.statusCode}")
        log.info("body = ${toBodilessEntity?.body}")

        log.info("Going to shutdown now!")
        System.exit(0)
    }

    fun String.encode() = URLEncoder.encode(this, Charsets.UTF_8)
}