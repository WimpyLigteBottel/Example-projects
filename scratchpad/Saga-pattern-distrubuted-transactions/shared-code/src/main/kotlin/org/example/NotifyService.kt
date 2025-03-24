package org.example

import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry
import java.time.Duration

open class NotifyService(
    mainServerUrl: String
) {

    private val webClient: WebClient = WebClient.create(mainServerUrl)

    fun notifyMainServer(action: Action) {

        webClient.post()
            .uri("/listen")
            .bodyValue(action)
            .retrieve()
            .toBodilessEntity()
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(10)))
            .block()

    }
}