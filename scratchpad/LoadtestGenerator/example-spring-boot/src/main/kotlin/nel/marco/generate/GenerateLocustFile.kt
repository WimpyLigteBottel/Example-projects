package nel.marco.generate

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping


@RestController
class GenerateLocustFile(
    val requestMappingHandlerMapping: RequestMappingHandlerMapping,
) {

    val log = LoggerFactory.getLogger(this::class.java)


    @GetMapping("/generate")
    fun generateLocust(): String {

        val base = """
            import random
            import pandas as pd
            from locust import HttpUser, task, between, constant_throughput

            class WebsiteUser(HttpUser):
                wait_time = constant_throughput(1)  # wait time between requests, in seconds
                
                #Need this otherwise wrong request content-type octet will be used
                defaultHeaders = {
                    'Content-Type': 'application/json'
                }
            """.trimIndent()


        var readInputs = mutableListOf<String>()
        var methods = mutableListOf<String>()

        requestMappingHandlerMapping.handlerMethods
            .filter {
                val patterns = it.key.patternValues.firstOrNull()
                patterns?.contains("/v1") ?: false

            }
            .forEach {

                val method = it.key.methodsCondition.methods.first()
                val pattern = it.key.patternValues.firstOrNull()?.replaceFirst("/", "")
                val beanName = it.value.bean
                val beanMethod = it.value.method.name

                val pythonMethodName = when (method) {
                    RequestMethod.GET -> "get"
                    RequestMethod.POST -> "post"
                    RequestMethod.PUT -> "put"
                    RequestMethod.DELETE -> "delete"
                    else -> TODO()
                }

                val dfName = beanMethod

                readInputs.add(
                    """
                     # Setup your csv file
                     $dfName = readFile("$dfName.csv")
                    """
                )


                methods.add(
                    """
                     @task(1)
                     def ${beanName}_${beanMethod}(self):
                          #Selects random row
                          row = self.$dfName.sample(n=1)
                          
                          #below is samples of access column names
                          id = row['columnA'].iloc[0]
                          body = row['columnB'].iloc[0]
                          
                                
                          #Replace path variables with {1} / {2} and variable
                          self.client.$pythonMethodName("$pattern".formated(id),data=body,headers=self.defaultHeaders)
                    """
                )


            }


        val stringBuilder = StringBuilder()

        stringBuilder
            .append(base)

        readInputs.forEach {
            stringBuilder
                .append("\n")
                .append(it)
        }

        methods.forEach {
            stringBuilder
                .append("\n")
                .append(it)
        }

        log.info("XXXXXXXXXXXXXXXXXXXXXXXX")
        log.info(stringBuilder.toString())
        log.info("XXXXXXXXXXXXXXXXXXXXXXXX")

        return stringBuilder.toString()
    }

    @GetMapping("/generate2")
    fun generateLocust(modelBase: List<GenerateModelBase>): String {

        val base = """
            
            import random
            import pandas as pd
            from locust import HttpUser, task, between, constant_throughput

            class WebsiteUser(HttpUser):
                wait_time = constant_throughput(1)  # wait time between requests, in seconds
                
                #Need this otherwise wrong request content-type octet will be used
                defaultHeaders = {
                    'Content-Type': 'application/json'
                }
            """.trimIndent()

        val stringBuilder = StringBuilder()

        stringBuilder
            .append(base)

        modelBase.forEach {

            val method = it.method
            val pattern = it.url.replaceFirst("/", "")
            val beanMethod = it.methodName



            stringBuilder.append(
                    """
                     # Setup your csv file
                     ${method}_$beanMethod = readFile("${method}_$beanMethod.csv")
                     
                    """
            )
                .append("\n")


            stringBuilder.append(
                    """
                     @task(1)
                     def ${method}_${beanMethod}(self):
                          #Selects random row
                          row = self.$beanMethod.sample(n=1)
                          
                          #below is samples of access column names
                          id = row['columnA'].iloc[0]
                          body = row['columnB'].iloc[0]
                          
                                
                          #Replace path variables with {1} / {2} and variable
                          self.client.$method("$pattern".format(id),data=body,headers=self.defaultHeaders)
                    """
            ).append("\n")


        }

        log.info("XXXXXXXXXXXXXXXXXXXXXXXX")
        log.info(stringBuilder.toString())
        log.info("XXXXXXXXXXXXXXXXXXXXXXXX")

        return stringBuilder.toString()
    }
}