package nel.marco

import org.springframework.core.annotation.Order
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


//@Component
@Order(10)
class CustomWebFilterOrder : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<java.lang.Void> {
        return chain.filter(exchange).doOnSuccess {
            println("Hello i have run 1st")
        }
    }
}


//@Component
@Order(20)
class CustomWebFilter2Order : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<java.lang.Void> {
        return chain.filter(exchange).doOnSuccess {
            println("Hello i have run 2nd")
        }
    }
}

//@Component
@Order(30)
class CustomWebFilter3Order : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<java.lang.Void> {
        return chain.filter(exchange).doOnSuccess {
            println("Hello i have run 3rd")
        }
    }
}