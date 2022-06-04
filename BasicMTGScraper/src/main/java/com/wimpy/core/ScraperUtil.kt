package com.wimpy.core

import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@Component
open class ScraperUtil constructor(private var cookies: MutableMap<String, String> = mutableMapOf()) {

    init {
        runBlocking {
            setupDefaultCookies()
        }

    }

    private val log = LoggerFactory.getLogger(ScraperUtil::class.java)

    /**
     * Retrieves the document via jsoup online which can be parsed
     * @param link
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun retrieveOnline(link: String): Document {
        val execute = Jsoup.connect(link).cookies(cookies).execute()
        // Updates cookies after every request
        cookies.putAll(execute.cookies())
        return Jsoup.parse(execute.body())
    }

    /**
     * Setup the default cookies to scrap future request a lot faster
     */
     suspend fun setupDefaultCookies() = try {
        val cookiesFromJsoup = Jsoup.connect(
            "https://www.mtggoldfish.com/price/Throne+of+Eldraine/Edgewall+Innkeeper#paper"
        )
            .execute()
            .cookies()


        cookies.putAll(cookiesFromJsoup)
    } catch (e: IOException) {
        log.error("Failed to update default cookies [errorMessage={}]", e.message, e)
    }

}