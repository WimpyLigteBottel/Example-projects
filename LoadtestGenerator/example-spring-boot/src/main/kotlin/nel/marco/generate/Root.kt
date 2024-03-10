package nel.marco.generate


data class Root(var contexts: Contexts? = null)

data class Application(var mappings: Mappings? = null)

data class Contexts(var application: Application? = null)

data class Details(
    var handlerMethod: HandlerMethod? = null,
    var requestMappingConditions: RequestMappingConditions? = null,
)

data class DispatcherServlet(
    var handler: String? = null,
    var predicate: String? = null,
    var details: Details? = null,
)

data class DispatcherServlets(
    var dispatcherServlet: ArrayList<DispatcherServlet>? = null
)

data class HandlerMethod(
    var className: String? = null,
    var name: String? = null,
    var descriptor: String? = null,
)

data class Mappings(
    var dispatcherServlets: DispatcherServlets? = null,
    var servletFilters: ArrayList<ServletFilter>? = null,
    var servlets: ArrayList<Servlet>? = null,
)

data class Produce(
    var mediaType: String? = null,
    var negated: Boolean = false,
)

data class RequestMappingConditions(
    var consumes: ArrayList<Any>? = null,
    var headers: ArrayList<Any>? = null,
    var methods: ArrayList<String>? = null,
    var params: ArrayList<Any>? = null,
    var patterns: ArrayList<String>? = null,
    var produces: ArrayList<Produce>? = null,
)


data class Servlet(
    var mappings: ArrayList<String>? = null,
    var name: String? = null,
    var className: String? = null,
)

data class ServletFilter(
    var servletNameMappings: ArrayList<Any>? = null,
    var urlPatternMappings: ArrayList<String>? = null,
    var name: String? = null,
    var className: String? = null,
)

