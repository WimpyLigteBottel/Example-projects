package nel.marco.endpoint;

import lombok.RequiredArgsConstructor;
import nel.marco.manager.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class ServiceDiscoveryImpl implements ServiceDiscovery {

  private final ServiceManager serviceManager;

  @Override
  public ResponseEntity<Boolean> registerService(String serviceName, String serviceUrl) {
    return ResponseEntity.ok(serviceManager.registerService(serviceName, serviceUrl));
  }

  @Override
  public ResponseEntity<Long> listServiceCount(String serviceName) {
    return ResponseEntity.ok(serviceManager.listServiceCount(serviceName));
  }

  @Override
  public ResponseEntity<Boolean> removeService(String serviceName, String serviceUrl) {
    return ResponseEntity.ok(serviceManager.removeService(serviceName, serviceUrl));
  }

  @Override
  public ResponseEntity<String> sendRequest(
      String serviceName,
      String path,
      String queryParameters,
      String body,
      MultiValueMap<String, String> headersMap,
      HttpServletRequest request,
      HttpServletResponse httpServletResponse) {
    return serviceManager.sendRequest(
        serviceName, path, queryParameters, body, headersMap, request, httpServletResponse);
  }

}
