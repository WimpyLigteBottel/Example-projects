package me.marco

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@SpringBootApplication
open class Launcher

fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}

@Service
open class RunOnStartup(
    private val publisher: ApplicationEventPublisher
) : ApplicationRunner {

    override fun run(args: ApplicationArguments): Unit {
        publisher.publishEvent(Event.LoggingEvent("Hello World"))
        System.exit(0)
    }
}

sealed class Event {
    class LoggingEvent(val message: String) : Event()
}


@Component
open class LoggingEventListener() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener
    @Order(3)
    open fun listen(event: Event.LoggingEvent) {
        logger.info("\uD83D\uDC64 Received event: ${event.message}")
    }

    @EventListener
    @Order(1)
    open fun listenWarn(event: Event.LoggingEvent) {
        logger.warn("\uD83D\uDC64 Received event: ${event.message}")
    }

    @EventListener
    @Order(2)
    open fun listenError(event: Event.LoggingEvent) {
        logger.error("\uD83D\uDC64 Received event: ${event.message}")
    }

}
