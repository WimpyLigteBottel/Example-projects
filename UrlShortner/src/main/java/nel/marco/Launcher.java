package nel.marco;

import nel.marco.util.ServiceDiscoveryStartupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Launcher {

  public static void main(String[] args) {
    SpringApplication.run(Launcher.class, args);
  }
}

@RestController
@EnableScheduling
class RestEndpoint {

  @Value("${ownserverurl}")
  private String ownServerUrl;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final ExecutorService executor = Executors.newFixedThreadPool(1);

  private final Map<String, Map.Entry<String, LocalDateTime>> map = new HashMap<>(10000);

  @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
  public void cleanup() {
    executor.submit(
        () -> {
          var stringEntryMap = Map.copyOf(map);
          LocalDateTime now = LocalDateTime.now();
          stringEntryMap.forEach(
              (s, pair) -> {
                if (now.isAfter(pair.getValue())) {
                  map.remove(s);
                }
              });

          MessageFormat messageFormat = new MessageFormat("Map has been cleaned [currentsize={0}]");
          String text = messageFormat.format(new Object[] {map.size()});
          logger.info(text);
        });
  }

  @GetMapping("/fillMap")
  public ResponseEntity<String> fillInternalMap() {

    for (int i = 0; i < 1000000; i++) {
      String encodedText = generateUniquePattern();

      map.put(encodedText, Map.entry(encodedText, LocalDateTime.now().plusSeconds(5)));
    }

    MessageFormat messageFormat = new MessageFormat("Map has been filled [size={0}]");
    String text = messageFormat.format(new Object[] {map.size()});
    return ResponseEntity.ok(text);
  }

  @GetMapping("/generate")
  public ResponseEntity<String> generate(@RequestParam String text) {
    String encodedText = generateUniquePattern();

    map.put(encodedText, Map.entry(text, LocalDateTime.now().plusSeconds(60)));

    return ResponseEntity.ok(ownServerUrl+encodedText);
  }

  private String generateUniquePattern() {
    String encodedText = UUID.randomUUID().toString().substring(0, 6);

    while (map.get(encodedText) != null) {
      encodedText = UUID.randomUUID().toString().substring(0, 6);
    }

    return encodedText;
  }

  @GetMapping("/{url}")
  public String getUrl(
      @PathVariable(value = "url") String url, HttpServletResponse httpServletResponse) {

    var pair = map.get(url);
    if (pair == null) {
      return "NO SHORTCODE FOR THIS";
    }

    // retrieve the decodedUrl and update the map to extend the url use
    String decodedUrl = pair.getKey();
    LocalDateTime extendedDuration = LocalDateTime.now().plusMinutes(1);
    map.put(url, Map.entry(decodedUrl, extendedDuration));
    logger.info("extending the url [key={};value={};until={}]", url, decodedUrl, extendedDuration);

    // redirect the user
    httpServletResponse.setHeader("Location", decodedUrl);
    httpServletResponse.setStatus(302);

    return decodedUrl;
  }
}
