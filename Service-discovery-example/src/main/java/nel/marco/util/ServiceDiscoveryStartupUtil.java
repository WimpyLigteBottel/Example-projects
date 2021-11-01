package nel.marco.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * This util is for other project that will be importing this project and this class will help
 * register the service.
 */
public class ServiceDiscoveryStartupUtil {

  private static final Logger logger = LoggerFactory.getLogger(ServiceDiscoveryStartupUtil.class);

  public static void registerService(
          String localServerPort, String discoveryServicePort, String applicationName) {

    String localHost = String.format("http://localhost:%s", localServerPort);

    String url =
            String.format(
                    "http://localhost:%s/service/register?serviceName=%s&serviceUrl=%s",
                    discoveryServicePort, applicationName, localHost);

    try {
      ResponseEntity<String> forEntity = new RestTemplate().getForEntity(url, String.class);

      if (forEntity.getStatusCode().is2xxSuccessful()) {
        logger.info("The service been registered [isRegistered={}]", forEntity.getBody());
      } else {
        logger.info("The service been registered [isRegistered={}]", false);
      }
    } catch (Exception e) {
      logger.info("service is down");
    }
  }


  public static void deregisterService(
          String localServerPort, String discoveryServicePort, String applicationName) {

    String localHost = String.format("http://localhost:%s", localServerPort);

    String url =
            String.format(
                    "http://localhost:%s/service/remove?serviceName=%s&serviceUrl=%s",
                    discoveryServicePort, applicationName, localHost);

    try {
      ResponseEntity<String> forEntity = new RestTemplate().getForEntity(url, String.class);

      if (forEntity.getStatusCode().is2xxSuccessful()) {
        logger.info("The service been deregistered [isRegistered={}]", forEntity.getBody());
      } else {
        logger.info("The service been deregistered [isRegistered={}]", false);
      }
    } catch (Exception e) {
      logger.info("service is down");
    }
  }
}
