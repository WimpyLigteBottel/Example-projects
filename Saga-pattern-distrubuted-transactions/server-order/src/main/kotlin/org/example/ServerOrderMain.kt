package org.example

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import java.util.*


@SpringBootApplication
open class ServerOrderMain


fun main() {
    runApplication<ServerOrderMain>()
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
){

    override fun getRandomState(): State {
        Random().nextInt(0, 100)
            .let {

                if( it < 20){
                    return State.PENDING
                }
                if (it < 30) {
                    return State.FAILED
                }

                return State.SUCCESS
            }
    }
}
