package nel.marco

import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.UUID

const val CUSTOM_ID = "CUSTOM-ID"

//@Component
class CustomWebFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<java.lang.Void> {
        return chain.filter(exchange).doOnSuccess {
            println("CUSTOM_ID " + exchange.getAttribute(CUSTOM_ID))
        }
    }
}

//@Component
class CustomContextFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<java.lang.Void> {
        return chain.filter(exchange).doOnSuccess {
            println("CUSTOM-ID ADDED")
            exchange.attributes[CUSTOM_ID] = UUID.randomUUID().toString()
        }
    }
}


