package me.marco.config

import me.marco.order.OrderClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.registry.ImportHttpServices

@Configuration
@ImportHttpServices
open class ClientConfig(

) {
    @Bean
    open fun httpServiceProxyFactory(@Value("\${custom.server.url}") url: String): HttpServiceProxyFactory {
        val adapter = RestClientAdapter.create(RestClient.create(url))
        return HttpServiceProxyFactory.builderFor(adapter).build()
    }

    @Bean
    open fun orderClient(factory: HttpServiceProxyFactory): OrderClient {
        return factory.createClient(OrderClient::class.java)
    }
}

