package nel.marco.intro

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import java.lang.IO.println

@RestController
class Controller(
    val primaryBean: String,
) {

    @PostConstruct
    fun setup() {
        println(primaryBean)
    }
}

@Configuration
class ConfigExample {


    @Bean
//    @Primary
    fun primaryBean(): String {
        return "hello world"
    }


    @Bean
    fun secondaryBean(): String {
        return "hello secondary bean"
    }

}