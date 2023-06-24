package org.example

import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient

open class NotifyService(
    mainServerUrl: String
) {

    private val webClient: WebClient = WebClient.create(mainServerUrl)

    private val log = LoggerFactory.getLogger(this::class.java)

    fun notifyMainServer(actionAndState: ActionAndState) {

        webClient.post()
            .uri("/listen")
            .bodyValue(actionAndState)
            .retrieve()
            .toBodilessEntity()
            .retry(3)
            .block()

    }
}