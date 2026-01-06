package me.marco

import me.marco.event.OrderCommandHandler
import me.marco.event.models.Command
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

@SpringBootApplication
@EnableAsync
@EnableScheduling
open class Launcher

fun main(args: Array<String>) {

    runApplication<Launcher>(*args)
}


@Component
class CommandLine(
    private val commandHandler: OrderCommandHandler
) {


    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    fun run() {
        val orderId = UUID.randomUUID().toString()

        val successFlow: List<Command> = listOf(
            Command.CreateOrderCommand(orderId),
            Command.AddItemCommand(orderId, "item-1", "Laptop", 999.99, 1),
            Command.AddItemCommand(orderId, "item-2", "Mouse", 29.99, 2),
            Command.ClearOrderCommand(orderId),
            Command.MarkOrderAsPaidCommand(orderId, "Credit Card"),
        )

        val orderId2 = UUID.randomUUID().toString()

        val itemTooLongInOrder: List<Command> = listOf(
            Command.CreateOrderCommand(orderId2),
            Command.AddItemCommand(orderId2, "item-A", "Laptop", 999.99, 1),
            Command.AddItemCommand(orderId2, "item-B", "Mouse", 29.99, 2),
        )


        successFlow.forEach {
            commandHandler.handle(it)
        }

        itemTooLongInOrder.forEach {
            commandHandler.handle(it)
        }

        Thread.sleep(6000)
        println(commandHandler.getOrder(orderId2))
    }

}