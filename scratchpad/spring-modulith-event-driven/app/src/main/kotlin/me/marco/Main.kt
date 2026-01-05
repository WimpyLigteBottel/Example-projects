package me.marco

import me.marco.actions.OrderCommandHandler
import me.marco.actions.models.Command
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import java.util.*

@SpringBootApplication
open class Launcher

fun main(args: Array<String>) {

    runApplication<Launcher>(*args)
}


@Component
class CommandLine(
    private val commandHandler: OrderCommandHandler
) : CommandLineRunner {


    override fun run(vararg args: String) {
        val orderId = UUID.randomUUID().toString()

        val events: List<Command> = listOf(
            Command.CreateOrderCommand(orderId),
            Command.AddItemCommand(orderId, "item-1", "Laptop", 999.99, 1),
            Command.AddItemCommand(orderId, "item-2", "Mouse", 29.99, 2),
            Command.ClearOrderCommand(orderId),
            Command.MarkOrderAsPaidCommand(orderId, "Credit Card"),
            // These should fail
            Command.ClearOrderCommand(orderId),
            Command.AddItemCommand(orderId, "item-11", "Keyboard", 79.99, 1)
        )

        events.forEach {
            commandHandler.handle(it)
        }


        println(commandHandler.getOrder(orderId))

        System.exit(0)

    }

}