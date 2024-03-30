package nel.marco


import com.google.api.core.ApiFuture
import com.google.api.gax.core.NoCredentialsProvider
import com.google.api.gax.grpc.GrpcTransportChannel
import com.google.api.gax.rpc.FixedTransportChannelProvider
import com.google.api.gax.rpc.TransportChannelProvider
import com.google.cloud.pubsub.v1.*
import com.google.cloud.spring.core.DefaultGcpProjectIdProvider
import com.google.cloud.spring.pubsub.PubSubAdmin
import com.google.protobuf.ByteString
import com.google.pubsub.v1.*
import io.grpc.ManagedChannelBuilder
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


@SpringBootApplication
open class Launcher

fun main() {
    runApplication<Launcher>()
}


@Configuration
open class ConfigurationSetup() {

    @Bean
    open fun pubSubAdmin(
        subscriptionAdminClient: SubscriptionAdminClient,
        topicAdminClient: TopicAdminClient
    ): PubSubAdmin {

        try {

            val pubSubAdmin = PubSubAdmin(DefaultGcpProjectIdProvider(), topicAdminClient, subscriptionAdminClient)

            // create topic
            val project = "example"
            val topic = TopicName.of(project, "mytopic")
            pubSubAdmin.createTopic(topic.toString())


            // create subscription to pull
            val pullSubscription = SubscriptionName.of(project, "pull_subscription")


            subscriptionAdminClient.createSubscription(
                pullSubscription,
                topic,
                PushConfig.getDefaultInstance(),
                10
            )

            val pushSubscriptionName = SubscriptionName.of(project, "push_subscription")

            subscriptionAdminClient.createSubscription(
                pushSubscriptionName,
                topic,
                PushConfig.getDefaultInstance(),
                10
            )

            return pubSubAdmin
        } catch (e: Exception) {
            throw RuntimeException("Failed to create PubSubAdmin", e)
        }
    }

    @Bean
    open fun transportChannelProvider(): TransportChannelProvider {
        val channel = ManagedChannelBuilder.forTarget("127.0.0.1:8085").usePlaintext().build()

        val channelProvider: TransportChannelProvider = FixedTransportChannelProvider
            .create(GrpcTransportChannel.create(channel))
        return channelProvider
    }

    @Bean
    open fun subscriptionAdminClient(channelProvider: TransportChannelProvider): SubscriptionAdminClient? =
        SubscriptionAdminClient
            .create(
                SubscriptionAdminSettings.newBuilder()
                    .setTransportChannelProvider(channelProvider)
                    .setCredentialsProvider(NoCredentialsProvider.create()).build()
            )

    @Bean
    open fun topicAdminClient(channelProvider: TransportChannelProvider): TopicAdminClient? {
        val topicAdminClient = TopicAdminClient.create(
            TopicAdminSettings.newBuilder()
                .setTransportChannelProvider(channelProvider)
                .setCredentialsProvider(NoCredentialsProvider.create()).build()
        )
        return topicAdminClient
    }


}

@RestController
open class HelloWorld(private val channelProvider: TransportChannelProvider) {

    private val log = LoggerFactory.getLogger(this::class.java)


    private val projectID = "example"
    private val topicID = "mytopic"
    private val pushSubscriptionID = "push_subscription"
    private val pullSubscriptionID = "pull_subscription"

    @GetMapping
    fun helloWorld(): String {

        publishDefaultMessage()

        return "Hello"
    }

    @GetMapping("/listen")
    fun helloworld2(){
        listen()
    }

    private fun listen() {
        val receiver = MessageReceiver { m: PubsubMessage, consumer: AckReplyConsumer ->
            // Handle incoming message, then ack the received message.
            println("Id: " + m.messageId)
            println("Data: " + m.data.toStringUtf8())
            consumer.ack()
        }

        val projectSubscriptionName = ProjectSubscriptionName.of(projectID, pullSubscriptionID)

        var subscriber: Subscriber? = null
        try {
            subscriber = Subscriber.newBuilder(projectSubscriptionName, receiver)
                .setCredentialsProvider(NoCredentialsProvider.create())
                .setChannelProvider(channelProvider).build()
            subscriber.startAsync().awaitRunning()
            System.out.printf("Listening for messages on %s:\n", projectSubscriptionName.toString())
            subscriber.awaitTerminated(10, TimeUnit.SECONDS)
        } catch (timeoutException: TimeoutException) {
            subscriber?.stopAsync()
        }
    }

    private fun publishDefaultMessage() {
        val publisher = Publisher.newBuilder(TopicName.ofProjectTopicName(projectID, topicID))
            .setCredentialsProvider(NoCredentialsProvider.create())
            .setChannelProvider(channelProvider).build()

        val message = "Hello World!"
        val data = ByteString.copyFromUtf8(message)
        val pubsubMessage = PubsubMessage.newBuilder().setData(data).build()

        val messageIdFuture: ApiFuture<String> = publisher.publish(pubsubMessage)
        val messageId = messageIdFuture.get()
        println("Published message ID: $messageId")
    }

}