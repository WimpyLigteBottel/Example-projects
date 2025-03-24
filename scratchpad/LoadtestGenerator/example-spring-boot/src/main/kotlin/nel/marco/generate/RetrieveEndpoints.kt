package nel.marco.generate

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.util.regex.Matcher
import java.util.regex.Pattern


@RestController
class RetrieveEndpoints(
) {

    val log = LoggerFactory.getLogger(this::class.java)


    @GetMapping("/retrieve")
    fun generateLocust(): String {


        val root = RestClient.create()
            .get()
            .uri("http://localhost:8090/actuator/mappings")
            .retrieve()
            .body(Root::class.java)


        val models = root?.contexts?.application?.mappings?.dispatcherServlets
            ?.dispatcherServlet?.mapNotNull {

                val predicate = it.predicate

                val methodType = when (predicate?.substring(0, 3)) {
                    "{GE" -> "get"
                    "{PU" -> "put"
                    "{DE" -> "delete"
                    "{PO" -> "post"
                    else -> ""
                }

                if (methodType == "")
                    return@mapNotNull null

                var uri = ""
                var methodName = it.details?.handlerMethod?.name ?: "random"


                // Define the regex pattern
                val pattern = "\\[(.*?)\\]"


                // Create a Pattern object
                val regex = Pattern.compile(pattern)


                // Create a Matcher object
                val matcher: Matcher = regex.matcher(predicate)

                matcher.find()
                uri = matcher.group()


                log.info("Method={}; url={}", methodType, matcher.group())

                GenerateModelBase(methodType, uri, methodName)
            } ?: emptyList()




        return GenerateLocustFile(RequestMappingHandlerMapping()).generateLocust(models)

    }
}