package me.marco.postgress

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Component

@SpringBootApplication
@EnableScheduling
open class Launcher

fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}


@Component
@Profile("!docker")
open class StartupRunner : ApplicationRunner {

    @Autowired
    lateinit var orderNotificationListener: OrderNotificationListener

    override fun run(args: ApplicationArguments) {
        orderNotificationListener.notify("All other 'listeners' will see this message :)")
    }
}
