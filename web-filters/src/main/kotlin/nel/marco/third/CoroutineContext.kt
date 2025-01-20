package nel.marco.third

import kotlinx.coroutines.reactor.asCoroutineContext
import kotlinx.coroutines.runBlocking
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.*
import kotlin.coroutines.CoroutineContext


class CustomContext(private val customId: String) : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<CustomContext>

    override val key: CoroutineContext.Key<*> = Key

    fun getCustomId(): String = customId
}

//@Component
@Order(1)
class CoroutineCustomWebFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain
            .filter(exchange)
            .contextWrite { ctx ->
                println("UPDATING CustomContext ")
                ctx.put(CustomContext, CustomContext(UUID.randomUUID().toString()))
            }
    }
}

//@Component
@Order(2)
class CoroutineCustomContextFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(exchange).doOnEach { signal ->
            val ctx = signal.contextView.asCoroutineContext()
            runBlocking(ctx) {
                val value: CustomContext = ctx.context.get(CustomContext)
                println("CUSTOM_ID: ${value.getCustomId()}")
            }
        }
    }
}


