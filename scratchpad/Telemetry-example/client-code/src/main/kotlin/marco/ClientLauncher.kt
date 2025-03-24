package marco

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import java.time.OffsetDateTime
import java.util.*


@SpringBootApplication
class ClientLauncher

fun main(args: Array<String>) {
    runApplication<ClientLauncher>(*args)
}

@RestController
class ServerSide(
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/report")
    fun helloWorld(@RequestParam allParams: Map<String, Any>): String {
        log.info("received: ${allParams}")
        return "$allParams"
    }

    @GetMapping("/report/{path}")
    fun helloWorld(
        @PathVariable("path") path: String,
        @RequestParam allParams: Map<String, Any>,
        @RequestParam names: List<String>
    ): String {
        log.info("-------- backend --------")
        allParams.forEach {
            log.info("${it.key} -> ${it.value}")
        }
        log.info("path: $path")
        log.info("names: $names")
        log.info("-------- backend --------")
        return "$allParams"
    }

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
            .uri("/{pathParam}") { uri ->
                val names = listOf("A+", "B,", "C&", "D:","! * ' ( ) ; : @ & = + \$ , / ? % # [ ]")
                val variables = linkedMapOf<String, Any>(
                    "pathParam" to UUID.randomUUID().toString(),
                    "version" to "1.0.0",
                    "config" to "<custom>",
                    "application-name" to "<application-name>",
                    "offsetDateTime" to OffsetDateTime.now(),
                    "datesTimes" to listOf(
                        OffsetDateTime.now(),
                        OffsetDateTime.now(),
                    ).toTypedArray(),
                    "names" to names,
                )
                uri.queryParam("version", "{version}")
                uri.queryParam("config", "{config}")
                uri.queryParam("application-name", "{application-name}")
                uri.queryParam("offsetDateTime", "{offsetDateTime}")
                uri.queryParam("datesTimes", "{datesTimes}")

                names.forEachIndexed { index, s ->
                    variables["names$index"] = s
                    uri.queryParam("names", "{names$index}")
                }

                val uriCompiled = uri.build(variables)
                log.info("-------- Request --------")
                log.info("full uri == $uriCompiled")
                log.info("path == ${uriCompiled.path}")
                log.info("query == ${uriCompiled.query}")
                log.info("-------- Request --------")

                uriCompiled
            }
            .retrieve()
            .toEntity(String::class.java)
            .block()


        log.info("-------- client --------")
        log.info("status = ${toBodilessEntity?.statusCode}")
        log.info("body = ${toBodilessEntity?.body}")
        log.info("-------- client --------")

        System.exit(0)
    }

}