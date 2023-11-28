package nel.marco.person;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import nel.marco.auth.AccessType;
import nel.marco.auth.AuthenticationRestEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
public class PersonRestEndpoint {

    @Autowired
    AuthenticationRestEndpoint authenticationRestEndpoint;

    List<Person> people = new ArrayList<>();

    @GetMapping(value = "/person", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPerson(HttpServletRequest request) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = authenticationRestEndpoint.verifyLogin(request);
        } catch (Exception e) {
            return status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        String access = decodedJWT.getClaim("access").as(String.class);
        if (access.equalsIgnoreCase(AccessType.NONE.name())) {
            return status(HttpStatus.FORBIDDEN).build();
        }
        return ok(people);
    }

    @PostMapping("/person")
    public ResponseEntity<?> addPerson(@RequestBody Person body, HttpServletRequest request) {

        DecodedJWT decodedJWT;
        try {
            decodedJWT = authenticationRestEndpoint.verifyLogin(request);
        } catch (Exception e) {
            return status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        String access = decodedJWT.getClaim("access").as(String.class);
        if (access.equalsIgnoreCase(AccessType.NONE.name())
                || access.equalsIgnoreCase(AccessType.READ.name())) {
            return status(HttpStatus.FORBIDDEN).body("Incorrect access level");
        }

        people.add(body);

        return ok(people);
    }
}