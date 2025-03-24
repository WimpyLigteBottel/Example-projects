package nel.marco.endpoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nel.marco.manager.ServiceForwardingManager;
import nel.marco.manager.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ServiceDiscoveryImpl implements ServiceDiscovery {

    private final ServiceManager serviceManager;
    private final ServiceForwardingManager serviceForwardingManager;

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
        return serviceForwardingManager.sendRequest(
                serviceName, path, queryParameters, body, headersMap, request, httpServletResponse);
    }

}
