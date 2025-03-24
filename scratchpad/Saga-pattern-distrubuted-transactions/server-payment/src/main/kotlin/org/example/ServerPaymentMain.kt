package org.example

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import java.util.*


@SpringBootApplication
open class ServerPaymentMain


fun main() {
    runApplication<ServerPaymentMain>()
}

@Service
class NotifyMainService(@Value("\${main-server-url}") baseUrl: String) : NotifyService(baseUrl)


@RestController
class OrderController(
    notifyMainService: NotifyMainService,
    @Value("\${server.name}") name: String
) : ProcessController(
    notifyMainService = notifyMainService,
    processName = name
)
