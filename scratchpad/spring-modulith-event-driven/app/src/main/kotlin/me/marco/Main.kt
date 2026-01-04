package me.marco

import me.marco.order.Order
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity

@SpringBootApplication
open class Launcher

fun main(args: Array<String>) {

    runApplication<Launcher>(*args)
}


@Component
class CommandLine : CommandLineRunner {

    val client = RestClient.create("http://localhost:8080")

    override fun run(vararg args: String) {

        val id = client.get().uri("/orders/create").retrieve().toEntity<String>().body!!

        val addedItem = client.post().uri("/items") { uriBuilder ->
            uriBuilder.queryParam("orderId", id)
            uriBuilder.queryParam("orderItemId", "123")

            uriBuilder.build()

        }.retrieve().toBodilessEntity()

        val order = client.get().uri("/orders/$id").retrieve().toEntity<Order>()

        val payOrder = client.post().uri("/pay/${order.body?.id}").retrieve().toBodilessEntity()

    }

}