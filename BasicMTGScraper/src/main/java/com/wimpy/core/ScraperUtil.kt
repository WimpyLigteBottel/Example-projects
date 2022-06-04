package com.wimpy.core

import org.springframework.beans.factory.annotation.Autowired
import java.util.HashMap
import kotlin.Throws
import java.io.IOException
import org.jsoup.Jsoup
import com.wimpy.core.ScraperUtil
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
open class ScraperUtil @Autowired constructor(private var cookies:MutableMap<String, String>) {

    init {
        cookies = mutableMapOf()
        setupDefaultCookies()
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
    private fun setupDefaultCookies() {
        try {
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

}