package nel.marco

import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.io.IOException
import java.time.Duration


@Configuration
open class Config {

    @Bean
    open fun webClient(webclientBuilder: WebClient.Builder): WebClient {
        return webclientBuilder.build()
    }


    @Bean
    open fun driver(): ChromeDriver {
        return  ChromeDriver()
    }


}


@Component
open class ScraperUtil(
    private var webClient: WebClient,
    private val driver: WebDriver
) {

    private val log = LoggerFactory.getLogger(ScraperUtil::class.java)

    init {
        driver
            .manage()
            .timeouts().pageLoadTimeout(Duration.ofSeconds(5L))
    }


    @Throws(IOException::class)
    fun retrieveOnline(link: String, findElement: String): String? {

        driver.get(link)

        return driver.findElement(By.cssSelector(findElement)).text
    }

}