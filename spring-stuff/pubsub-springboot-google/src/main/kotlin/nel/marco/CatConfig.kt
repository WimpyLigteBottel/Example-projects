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
import org.springframework.integration.annotation.Poller
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service


@Configuration
class CatConfig() {


    //These are needed for RECEIVING MESSAGES
    @Bean
    fun catPubsubInputChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    fun catChannelUpdater(
        @Qualifier("catPubsubInputChannel") inputChannel: MessageChannel,
        pubSubSubscriberTemplate: PubSubSubscriberTemplate
    ): PubSubInboundChannelAdapter {
        val adapter = PubSubInboundChannelAdapter(pubSubSubscriberTemplate, subCat)
        adapter.outputChannel = inputChannel
        return adapter
    }
    //These are needed for RECEIVING MESSAGES


    //These are needed for SENDING MESSAGES
    @Bean
    @ServiceActivator(inputChannel = "catPubsubOutputChannel")
    fun catMessageSender(pubsubTemplate: PubSubTemplate?): MessageHandler {
        return PubSubMessageHandler(pubsubTemplate, topicCat)
    }
    //These are needed for SENDING MESSAGES


}


//These are needed for SENDING MESSAGES
@MessagingGateway(defaultRequestChannel = "catPubsubOutputChannel")
interface CatGateway {
    fun sendToPubsub(text: String?)
}
//These are needed for SENDING MESSAGES

@Service
class MessageReceiver(private val marcoGateway: MarcoGateway) {
    @ServiceActivator(
        inputChannel = "catPubsubInputChannel",
        poller = Poller(fixedRate = "5000")
    )
    fun receiveMessageCat(
        payload: String,
        @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) message: BasicAcknowledgeablePubsubMessage
    ) {
        println("cat received: $payload")
        message.ack()
        val newPayload = (payload.toInt() + 1).toString()
        println("cat sending: $newPayload")
        marcoGateway.sendToPubsub(newPayload)
    }
}