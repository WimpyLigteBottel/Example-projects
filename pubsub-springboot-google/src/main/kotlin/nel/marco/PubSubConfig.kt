package nel.marco

import com.google.cloud.spring.pubsub.PubSubAdmin
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.IntegrationComponentScan
import org.springframework.integration.config.EnableIntegration

val topicMarco = "topicMarco"
val subMarco = "subMarco"
val topicCat = "topicCat"
val subCat = "subCat"

@Configuration
@EnableIntegration
@IntegrationComponentScan
class PubSubConfig(val pubSubAdmin: PubSubAdmin) {
    @PostConstruct
    fun setup() {
        println("Creating topic and subscriptions")

        kotlin.runCatching { pubSubAdmin.createTopic(topicMarco) }
        kotlin.runCatching { pubSubAdmin.createSubscription(subMarco, topicMarco) }

        kotlin.runCatching { pubSubAdmin.createTopic(topicCat) }
        kotlin.runCatching { pubSubAdmin.createSubscription(subCat, topicCat) }
        println("done")
    }

}