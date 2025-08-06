package nel.marco.third

import nel.marco.second.CUSTOM_ID
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


@Order(HIGHEST_PRECEDENCE)
@Component
class CustomContextFilterContextWrite : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void> =
        chain.filter(exchange).contextWrite { ctx ->
            println("CUSTOM_ID has been added")
            ctx.put(CUSTOM_ID, exchange.getHeader())
        }


    fun ServerWebExchange.getHeader(): String {
        return this.getRequest().headers.get("CUSTOM-HEADER")?.single() ?: "EMPTY"
    }
}