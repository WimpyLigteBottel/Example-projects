package com.wimpy;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.tinylog.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {

  static Map<String, String> cookies = new HashMap<>();

  public static void main(String[] args) {
    LocalDateTime start = LocalDateTime.now();
    try {
      List<String> lines = Files.readAllLines(Paths.get("src/main/resources/cards"));

      if (cookies.isEmpty()) {
        cookies.putAll(
            Jsoup.connect(
                    "https://www.mtggoldfish.com/price/Throne+of+Eldraine/Edgewall+Innkeeper#paper")
                .cookies(cookies)
                .execute()
                .cookies());
      }

      for (int i = 1; i < lines.size(); i++) {

        String[] nameLinkPrice = lines.get(i).split("\\|");

        Connection.Response execute = Jsoup.connect(nameLinkPrice[1]).cookies(cookies).execute();
        // Updates cookies after every request
        cookies.putAll(execute.cookies());
        Document document = Jsoup.parse(execute.body());

        String updatedPrice =
            document
                .select(
                    "body > main > div > div.price-card-name-header > div > div > div.price-box.paper > div.price-box-price")
                .html()
                .replaceAll("&nbsp;", "");

        System.out.printf(
            "name:%s \n Link:%s \n oldPrice:%s \n updatePrice:%s \n\n",
            nameLinkPrice[0], nameLinkPrice[1], nameLinkPrice[2], updatedPrice);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    Logger.info(
        "method took this long [time={}ms]",
        Duration.between(start, LocalDateTime.now()).toMillis());
  }
}
