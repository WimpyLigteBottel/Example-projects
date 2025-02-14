package nel.marco.second

import java.util.UUID
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

const val CUSTOM_ID = "CUSTOM-ID"

//@Component
@Order(1)
class CustomWebFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(exchange).doOnSuccess {
            println("CUSTOM_ID " + exchange.getAttribute(CUSTOM_ID))
        }
    }
}

@Order(2)
//@Component
class CustomContextFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(exchange).doOnSuccess {
            val uuid = UUID.randomUUID().toString()
            println("CUSTOM-ID ADDED [uuid: $uuid]")
            exchange.attributes[CUSTOM_ID] = uuid
        }
    }
}


