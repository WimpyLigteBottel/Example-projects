package org.example

import org.slf4j.LoggerFactory
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry
import java.time.Duration

open class ProcessActionService(
    private val baseUrl: String,
    private val name: PendingActionName = PendingActionName.UNKNOWN
) {

    private val webClient: WebClient = WebClient.builder()
        .baseUrl(baseUrl)
        .clientConnector(ReactorClientHttpConnector())
        .build()
    private val log = LoggerFactory.getLogger(this::class.java)


    /**
     * Starts the process of orderCreation, this is fire and forget operation

     */
    fun createFireAndForget(id: String): Action {
        val action = Action(id, name)

        try {
            webClient.post()
                .uri("/create")
                .bodyValue(action)
                .retrieve()
                .bodyToMono(Void::class.java)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(10)))
                .subscribe()

            return action.pending()
        } catch (e: Exception) {
            log.error("failed to create [id=$id]", e)
        }

        return action.failed()
    }

    /**
     * This is blocking and waits till we get response to verify that it has rolled back
     */
    fun stopProcessingBlocking(action: Action): Boolean {
        val isSuccess = runCatching {
            webClient.post()
                .uri("$baseUrl/rollback")
                .bodyValue(action)
                .retrieve()
                .bodyToMono(Void::class.java)
                .retry(3)
                .block()

        }
        return isSuccess.isSuccess

    }

}