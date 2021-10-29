package nel.marco;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import nel.marco.controller.AuthenticationRestEndpoint;
import nel.marco.controller.type.AccessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Launcher {

  public static void main(String[] args) {
    SpringApplication.run(Launcher.class, args);
  }
}

@RestController
class RestEndpoint {

  @Autowired AuthenticationRestEndpoint authenticationRestEndpoint;

  List<Person> people = new ArrayList<>();

  @GetMapping("/person")
  public ResponseEntity<?> getPerson(HttpServletRequest request) {
    DecodedJWT decodedJWT;
    try {
      decodedJWT = authenticationRestEndpoint.verifyLogin(request);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    String access = decodedJWT.getClaim("access").as(String.class);
    if (access.equalsIgnoreCase(AccessType.NONE.name())) {
      return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    return ResponseEntity.ok(people);
  }

  @PostMapping("/person")
  public ResponseEntity<?> addPerson(@RequestBody String body, HttpServletRequest request) {

    DecodedJWT decodedJWT;
    try {
      decodedJWT = authenticationRestEndpoint.verifyLogin(request);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    String access = decodedJWT.getClaim("access").as(String.class);
    if (access.equalsIgnoreCase(AccessType.NONE.name())
        || access.equalsIgnoreCase(AccessType.READ.name())) {
      return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Incorrect access level");
    }

    Person person = new Gson().fromJson(body, Person.class);

    people.add(person);

    return ResponseEntity.ok(people);
  }
}
