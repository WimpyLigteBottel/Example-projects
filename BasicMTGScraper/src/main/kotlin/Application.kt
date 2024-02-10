import java.util.HashMap
import kotlin.jvm.JvmStatic
import java.time.LocalDateTime
import java.nio.file.Files
import java.nio.file.Paths
import org.jsoup.Jsoup
import java.lang.Exception

object Application {
    var cookies: MutableMap<String, String> = HashMap()
    @JvmStatic
    fun main(args: Array<String>) {
        val start = LocalDateTime.now()
        try {
            val lines = Files.readAllLines(Paths.get("src/main/resources/cards"))
            if (cookies.isEmpty()) {
                cookies.putAll(
                        Jsoup.connect(
                                "https://www.mtggoldfish.com/price/Throne+of+Eldraine/Edgewall+Innkeeper#paper")
                                .cookies(cookies)
                                .execute()
                                .cookies())
            }
            for (i in 1 until lines.size) {
                val nameLinkPrice = lines[i].split("\\|".toRegex()).toTypedArray()
                val execute = Jsoup.connect(nameLinkPrice[1]).cookies(cookies).execute()
                // Updates cookies after every request
                cookies.putAll(execute.cookies())
                val document = Jsoup.parse(execute.body())
                val updatedPrice = document
                        .select(
                                "body > main > div > div.price-card-name-header > div > div > div.price-box.paper > div.price-box-price")
                        .html()
                        .replace("&nbsp;".toRegex(), "")
                System.out.printf(
                        "name:%s \n Link:%s \n oldPrice:%s \n updatePrice:%s \n\n",
                        nameLinkPrice[0], nameLinkPrice[1], nameLinkPrice[2], updatedPrice)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}