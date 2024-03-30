package nel.marco

import com.google.api.gax.core.NoCredentialsProvider
import com.google.api.gax.grpc.GrpcTransportChannel
import com.google.api.gax.rpc.FixedTransportChannelProvider
import com.google.api.gax.rpc.TransportChannelProvider
import com.google.cloud.pubsub.v1.SubscriptionAdminClient
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings
import com.google.cloud.pubsub.v1.TopicAdminClient
import com.google.cloud.pubsub.v1.TopicAdminSettings
import com.google.cloud.spring.core.DefaultGcpProjectIdProvider
import com.google.cloud.spring.pubsub.PubSubAdmin
import com.google.pubsub.v1.SubscriptionName
import com.google.pubsub.v1.TopicName
import io.grpc.ManagedChannelBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "custom.pubsub")
open class PubsubProperties {
    lateinit var topic: String
    lateinit var subscription: String
}

@Configuration
open class ConfigurationSetup(
    @Value("\${spring.cloud.gcp.pubsub.project-id}") val projectId: String,
    @Value("\${spring.cloud.gcp.pubsub.emulator-host}") val host: String,
    val pubsubProperties: PubsubProperties
) {

    @Bean
    open fun topicName(): TopicName = TopicName.of(projectId, pubsubProperties.topic)

    @Bean
    open fun subscriptionName(): SubscriptionName = SubscriptionName.of(projectId, pubsubProperties.subscription)


    @Bean
    open fun pubSubAdmin(
        subscriptionAdminClient: SubscriptionAdminClient,
        topicAdminClient: TopicAdminClient
    ): PubSubAdmin =
        PubSubAdmin(DefaultGcpProjectIdProvider(), topicAdminClient, subscriptionAdminClient)


    @Bean
    open fun transportChannelProvider(): TransportChannelProvider {
        val channel = ManagedChannelBuilder.forTarget(host).usePlaintext().build()

        val channelProvider: TransportChannelProvider =
            FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel))
        return channelProvider
    }

    @Bean
    open fun subscriptionAdminClient(channelProvider: TransportChannelProvider): SubscriptionAdminClient =
        SubscriptionAdminClient.create(
            SubscriptionAdminSettings.newBuilder()
                .setTransportChannelProvider(channelProvider)
                .setCredentialsProvider(NoCredentialsProvider.create()).build()
        )

    @Bean
    open fun topicAdminClient(channelProvider: TransportChannelProvider): TopicAdminClient = TopicAdminClient.create(
        TopicAdminSettings.newBuilder()
            .setTransportChannelProvider(channelProvider)
            .setCredentialsProvider(NoCredentialsProvider.create()).build()
    )
}