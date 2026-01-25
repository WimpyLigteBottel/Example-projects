package me.marco.postgress

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@SpringBootApplication
@EnableScheduling
open class Launcher

fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}


@Component
open class StartupRunner : ApplicationRunner {

    @Autowired
    lateinit var orderNotificationListener: OrderNotificationListener

    override fun run(args: ApplicationArguments) {
        orderNotificationListener.notify("All other 'listeners' will see this message :)")
    }
}
