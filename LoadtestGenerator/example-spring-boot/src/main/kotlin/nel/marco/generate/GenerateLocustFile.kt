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
            from locust import HttpUser, task, between, constant
            
            #Reads the files and store it as dataframe
            def readFile(fileName):
                try:
                    # Read the CSV file into a DataFrame
                    df = pd.read_csv(fileName, sep=';')
                    return df
                except FileNotFoundError:
                    print("File not found. Please make sure the file exists.")
                    return None


            class WebsiteUser(HttpUser):
                wait_time = constant(1)  # wait time between requests, in seconds
                
                #Need this otherwise wrong request content-type octet will be used
                defaultHeaders = {
                    'Content-Type': 'application/json'
                }
            """.trimIndent()


        var methods = mutableListOf<String>()

        requestMappingHandlerMapping.handlerMethods
            .filter {
                val patterns = it.key.patternValues.firstOrNull()
                patterns?.contains("/v1") ?: false

            }
            .forEach {

                val method = it.key.methodsCondition.methods.first()
                val pattern = it.key.patternValues.firstOrNull()?.replaceFirst("/", "")
                val patternPythonSafe = pattern?.replace("/", "_")
                val beanName = it.value.bean
                val beanMethod = it.value.method.name

                val pythonMethodName = when (method) {
                    RequestMethod.GET -> "get"
                    RequestMethod.POST -> "post"
                    RequestMethod.PUT -> "put"
                    RequestMethod.DELETE -> "delete"
                    RequestMethod.PATCH -> TODO()
                    RequestMethod.HEAD -> TODO()
                    RequestMethod.OPTIONS -> TODO()
                    RequestMethod.TRACE -> TODO()
                }

                val dfName = "${pythonMethodName}_$patternPythonSafe"

                when (method) {
                    RequestMethod.GET -> {
                        methods.add(
                            """
                                $dfName = readFile("$dfName.csv")
                                
                                @task(1)
                                def ${beanName}_${beanMethod}(self):
                                    #Selects the "column" value from csv file which is read as dataframe
                                    name = random.choice(self.$dfName['Name'])
                                    
                                    #Groups the request under pattern
                                    self.client.request_name = "$pattern"
                                    
                                    #actual request
                                    self.client.$pythonMethodName("$pattern",params={'name': name},headers=self.defaultHeaders)
                                    
                                    #Remove the grouping name for other request
                                    self.client.request_name = None
                            """
                        )
                    }

                    else -> {
                        methods.add(
                            """
                                $dfName = readFile("$dfName.csv")
                                
                                @task(1)
                                def ${beanName}_${beanMethod}(self):
                                    #Selects the "column" value from csv file which is read as dataframe
                                    body = random.choice(self.$dfName['body'])
                                    
                                    #Groups the request under pattern
                                    self.client.request_name = "$pattern"
                                    
                                    #actual request
                                    self.client.$pythonMethodName("$pattern",data=body,headers=self.defaultHeaders)
                                    
                                    #Remove the grouping name for other request
                                    self.client.request_name = None
                            """
                        )
                    }
                }


            }


        val stringBuilder = StringBuilder()

        stringBuilder
            .append(base)

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
}