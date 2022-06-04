package com.wimpy.core;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ScraperUtil {

    private static final Logger log = LoggerFactory.getLogger(ScraperUtil.class);
    private final Map<String, String> cookies = new HashMap<>();

    @Autowired
    public ScraperUtil() {
        setupDefaultCookies();
    }


    /**
     * Retrieves the document via jsoup online which can be parsed
     * @param link
     * @return
     * @throws IOException
     */
    public Document retrieveOnline(String link) throws IOException {
        Connection.Response execute = Jsoup.connect(link).cookies(cookies).execute();
        // Updates cookies after every request
        cookies.putAll(execute.cookies());
        Document document = Jsoup.parse(execute.body());
        return document;
    }

    /**
     * Setup the default cookies to scrap future request a lot faster
     */
    private void setupDefaultCookies() {
        try {
            cookies.putAll(
                    Jsoup.connect(
                                    "https://www.mtggoldfish.com/price/Throne+of+Eldraine/Edgewall+Innkeeper#paper")
                            .cookies(cookies)
                            .execute()
                            .cookies());
        } catch (IOException e) {
            log.error("Failed to update default cookies [errorMessage={}]", e.getMessage(), e);
        }
    }
}
