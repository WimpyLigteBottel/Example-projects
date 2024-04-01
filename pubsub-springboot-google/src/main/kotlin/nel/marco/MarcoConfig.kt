package nel.marco

import com.google.cloud.spring.pubsub.core.PubSubTemplate
import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.MessagingGateway
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service

@Configuration
class MarcoConfig {

    @Bean
    fun marcoPubsubInputChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    fun marcoChannelUpdater(
        @Qualifier("marcoPubsubInputChannel") inputChannel: MessageChannel,
        pubSubSubscriberTemplate: PubSubSubscriberTemplate
    ): PubSubInboundChannelAdapter {
        val adapter = PubSubInboundChannelAdapter(pubSubSubscriberTemplate, subMarco)
        adapter.outputChannel = inputChannel
        return adapter
    }

    @Bean
    @ServiceActivator(inputChannel = "marcoPubsubOutputChannel")
    fun marcoMessageSender(pubsubTemplate: PubSubTemplate?): MessageHandler {
        return PubSubMessageHandler(pubsubTemplate, topicMarco)
    }


}

@MessagingGateway(defaultRequestChannel = "marcoPubsubOutputChannel")
interface MarcoGateway {

    fun sendToPubsub(text: String?)
}

@Service
class MessageReceiverMarco(private val catGateway: CatGateway) {
    @ServiceActivator(inputChannel = "marcoPubsubInputChannel")
    fun receiveMessageMarco(
        payload: String,
        @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) message: BasicAcknowledgeablePubsubMessage
    ) {
        println("marco received: $payload")
        Thread.sleep(5000)
        message.ack()
        val newPayload = (payload.toInt() + 1).toString()
        println("marco sending: $newPayload")
        catGateway.sendToPubsub(newPayload)
    }
}