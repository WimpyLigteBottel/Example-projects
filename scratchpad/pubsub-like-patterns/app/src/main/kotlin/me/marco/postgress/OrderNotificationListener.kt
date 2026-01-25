package me.marco.postgress

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.postgresql.PGConnection
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Service
import javax.sql.DataSource
import kotlin.concurrent.thread

@Service
class OrderNotificationListener(
    private val dataSource: DataSource,
    private val jdbcClient: JdbcClient,
) {
    private var listenerThread: Thread? = null
    private var running = false

    @PostConstruct
    fun startListening() {
        running = true
        listenerThread = thread(start = true) {
            // Get connection directly from DataSource
            dataSource.connection.use { conn ->
                val pgConn = conn.unwrap(PGConnection::class.java)

                // Subscribe to the channel
                conn.createStatement().execute("LISTEN order_created")
                println("Listening for order_created notifications...")

                while (running) {
                    pgConn.getNotifications(100)?.forEach { notification ->
                        println("📦 New message arrived!")
                        println("Channel: ${notification.name}")
                        println("Payload: ${notification.parameter}")
                    }
                }
            }
        }
    }

    fun notify(orderid: String) {
        val payload = """{"message": ${orderid}}"""
        jdbcClient.sql("SELECT pg_notify('order_created', :payload)")
            .param("payload", payload)
            .query()
            .singleValue()
    }

    @PreDestroy
    fun stopListening() {
        running = false
        listenerThread?.interrupt()
    }
}