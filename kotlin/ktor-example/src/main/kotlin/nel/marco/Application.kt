package nel.marco

import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main(args: Array<String>) {
    EngineMain.main(args)
}

var runningService = RunningService()

suspend fun Application.module() {
    routing {
        get("/start") {
            runningService.start()
            call.respondText("Started World!")
        }
        get("/stop") {
            runningService.stop()
            call.respondText("Stopped World!")
        }
    }
}


