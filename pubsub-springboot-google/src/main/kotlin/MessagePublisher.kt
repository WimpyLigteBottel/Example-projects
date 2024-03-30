package nel.marco

import com.google.api.gax.core.NoCredentialsProvider
import com.google.api.gax.rpc.TransportChannelProvider
import com.google.cloud.pubsub.v1.Publisher
import com.google.protobuf.ByteString
import com.google.pubsub.v1.PubsubMessage
import com.google.pubsub.v1.TopicName
import org.springframework.stereotype.Component

@Component
open class MessagePublisher(topicName: TopicName, transportChannelProvider: TransportChannelProvider) {

    val publisher: Publisher = Publisher.newBuilder(TopicName.ofProjectTopicName(topicName.project, topicName.topic))
        .setCredentialsProvider(NoCredentialsProvider.create())
        .setChannelProvider(transportChannelProvider)
        .build()


    fun publish(message: String) {
        val data = ByteString.copyFromUtf8(message)
        val pubsubMessage = PubsubMessage.newBuilder().setData(data).build()

        publisher.publish(pubsubMessage)
    }
}