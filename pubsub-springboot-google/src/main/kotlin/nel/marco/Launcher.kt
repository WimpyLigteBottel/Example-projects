package nel.marco

import com.google.cloud.spring.pubsub.core.PubSubTemplate
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.DependsOn
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@SpringBootApplication
class Launcher {}

fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}


@Component
class Runner(private val pubSubTemplate: PubSubTemplate) : CommandLineRunner {
    override fun run(vararg args: String?) {
        pubSubTemplate.publish(topicMarco, "1")
    }
}


