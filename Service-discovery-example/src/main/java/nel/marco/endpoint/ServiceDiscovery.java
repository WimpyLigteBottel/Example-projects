package nel.marco.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/service")
public interface ServiceDiscovery {

  @GetMapping("/register")
  ResponseEntity<Boolean> registerService(
      @RequestParam(required = true) String serviceName,
      @RequestParam(required = true) String serviceUrl);

  @GetMapping("/list")
  ResponseEntity<Long> listServiceCount(@RequestParam(required = true) String serviceName);

  @GetMapping("/remove")
  ResponseEntity<Boolean> removeService(
      @RequestParam(required = true) String serviceName,
      @RequestParam(required = true) String serviceUrl);

  @GetMapping("/send")
  ResponseEntity<String> sendRequest(
      @RequestParam(required = true) String serviceName,
      @RequestParam(required = true) String path,
      @RequestParam(required = true) String queryParameters,
      @RequestBody(required = false) String body,
      @RequestHeader(required = false) MultiValueMap<String, String> headersMap,
      HttpServletRequest request,
      HttpServletResponse httpServletResponse);
}
