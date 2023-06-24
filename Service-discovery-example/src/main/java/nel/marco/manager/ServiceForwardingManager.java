package nel.marco.manager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class ServiceForwardingManager {


    private ServiceManager serviceManager;

    private WebClient httpClient;

    public ServiceForwardingManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;

        httpClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector())
                .build();
    }


    public ResponseEntity<String> sendRequest(
            String serviceName,
            String path,
            String queryParameters,
            String body,
            MultiValueMap<String, String> headersMap,
            HttpServletRequest request,
            HttpServletResponse httpServletResponse) {


        String randomUrlInList = pickRandomUrlFromService(serviceName);
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());


        WebClient.RequestBodySpec client = httpClient
                .method(httpMethod)
                .uri(uriBuilder -> buildUrl(path, queryParameters, randomUrlInList, uriBuilder))
                .headers(httpHeaders -> httpHeaders.putAll(headersMap));

        if (body != null) {
            client.bodyValue(body);
        }


        return client
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    private static URI buildUrl(String path, String queryParameters, String randomUrlInList, UriBuilder uriBuilder) {
        String scheme = randomUrlInList.split(":")[0];
        String host = randomUrlInList.split(":")[1].substring(2);
        String port = randomUrlInList.split(":")[2];
        return uriBuilder
                .scheme(scheme) // http or https
                .host(host) // I need to strip // after http:
                .port(port)
                .path(path)
                .replaceQuery(queryParameters)
                .build();
    }


    private String pickRandomUrlFromService(String serviceName) {
        List<String> url = serviceManager.findService(serviceName);


        return url.get(ThreadLocalRandom.current().nextInt(0, url.size()));
    }
}
