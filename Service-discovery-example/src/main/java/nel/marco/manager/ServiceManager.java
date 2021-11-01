package nel.marco.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class ServiceManager {

  // This map is not threadsafe 100%, can implement reentrant lock or something
  Map<String, List<String>> serviceDictionary = new ConcurrentHashMap<>();

  public boolean registerService(String serviceName, String serviceUrl) {
    List<String> services = serviceDictionary.computeIfAbsent(serviceName, k -> new ArrayList<>());

    if (services.contains(serviceUrl)) {
      log.info("service registered [successful={};name={};url={}]", false, serviceName, serviceUrl);
      return false;
    }

    services.add(serviceUrl);

    serviceDictionary.put(serviceName, services);
    log.info("service registered [successful={};name={};url={}]", true, serviceName, serviceUrl);

    return true;
  }

  public long listServiceCount(String serviceName) {
    return serviceDictionary.getOrDefault(serviceName, new ArrayList<>()).size();
  }

  public boolean removeService(String serviceName, String serviceUrl) {

    List<String> services = serviceDictionary.get(serviceName);

    if (services == null || !services.contains(serviceUrl)) {
      log.info("service removed [successful={};name={};url={}]", false, serviceName, serviceUrl);
      return false;
    }

    services.remove(serviceUrl);

    if (services.isEmpty()) {
      serviceDictionary.remove(serviceName);
    } else {
      serviceDictionary.put(serviceName, services);
    }
    log.info("service removed [successful={};name={};url={}]", true, serviceName, serviceUrl);

    return true;
  }

  public ResponseEntity<String> sendRequest(
      String serviceName,
      String path,
      String queryParameters,
      String body,
      MultiValueMap<String, String> headersMap,
      HttpServletRequest request,
      HttpServletResponse httpServletResponse) {

    RestTemplate restTemplate = new RestTemplate();

    List<String> strings = serviceDictionary.get(serviceName);
    String randomUrlInList = strings.get(ThreadLocalRandom.current().nextInt(0, strings.size()));
    HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());

    ResponseEntity<String> exchange =
        restTemplate.exchange(
            randomUrlInList + path + "?" + queryParameters,
            httpMethod,
            new HttpEntity(body, headersMap),
            String.class);

    return exchange;
  }
}
