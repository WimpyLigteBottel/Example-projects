package org.example

import org.example.api.ProcessController
import org.example.internal.NotifyService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
open class ServerOrderMain


fun main() {
    runApplication<ServerOrderMain>()
}


@Service
class NotifyMainService(@Value($$"${main-server-url}") baseUrl: String) : NotifyService(baseUrl)


@RestController
class OrderController(
    notifyMainService: NotifyMainService,
    @Value($$"${server.name}") name: String
) : ProcessController(
    notifyMainService = notifyMainService,
    processName = name
)
