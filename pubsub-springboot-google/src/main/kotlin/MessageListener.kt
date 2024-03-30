package nel.marco

import com.google.api.gax.core.NoCredentialsProvider
import com.google.api.gax.rpc.TransportChannelProvider
import com.google.cloud.pubsub.v1.AckReplyConsumer
import com.google.cloud.pubsub.v1.MessageReceiver
import com.google.cloud.pubsub.v1.Subscriber
import com.google.cloud.spring.pubsub.PubSubAdmin
import com.google.pubsub.v1.ProjectSubscriptionName
import com.google.pubsub.v1.PubsubMessage
import com.google.pubsub.v1.SubscriptionName
import com.google.pubsub.v1.TopicName
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.TimeoutException

@Service
@DependsOn("configurationSetup")
open class MessageListener(
    private val pubSubAdmin: PubSubAdmin,
    private val subscriptionName: SubscriptionName,
    private val topicName: TopicName,
    private val transportChannelProvider: TransportChannelProvider
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val executors = Executors.newSingleThreadExecutor()

    private val receiver = MessageReceiver { m: PubsubMessage, consumer: AckReplyConsumer ->
        try {
            log.info("processed [id=${m.messageId};data=${m.data.toStringUtf8()}]")
            consumer.ack()
        } catch (e: Exception) {
            log.info("failed to process [id=${m.messageId};data=${m.data.toStringUtf8()}]")
            consumer.nack()
        }
    }

    @PostConstruct
    fun startListening() {
        runCatching {
            if (pubSubAdmin.getTopic(topicName.toString()) == null) {
                pubSubAdmin.createTopic(topicName.toString())
                log.info("topicName created  = $topicName")
            }

            if (pubSubAdmin.getSubscription(subscriptionName.toString()) == null) {
                pubSubAdmin.createSubscription(subscriptionName.toString(), topicName.toString())
                log.info("subscription created  = $subscriptionName")
            }
        }

        executors.submit(::setup)
    }

    fun setup() {
        val projectSubscriptionName =
            ProjectSubscriptionName.of(subscriptionName.project, subscriptionName.subscription)

        val subscriber = Subscriber.newBuilder(projectSubscriptionName, receiver)
            .setParallelPullCount(1)
            .setCredentialsProvider(NoCredentialsProvider.create())
            .setChannelProvider(transportChannelProvider)
            .build()
        try {
            subscriber.startAsync().awaitRunning()

            log.info("Listening for messages on {}", projectSubscriptionName.toString())

            subscriber.awaitTerminated()

            log.warn("DONE LISTENING TO THE TOPIC")
        } catch (timeoutException: TimeoutException) {
            subscriber?.stopAsync()
            log.warn("DONE LISTENING TO THE TOPIC")
        }
    }
}