package nel.marco.auth;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class AuthenticationRestEndpointTest {

    public TestRestTemplate testRestTemplate = new TestRestTemplate();

    @LocalServerPort
    public String port;


    @Test
    public void testLoginGeneration() {
        String url = "http://localhost:port/login/generate".replace("port", port);
        var forEntity = testRestTemplate.getForEntity(url, Token.class);

        Token actual = forEntity.getBody();

        assertFalse(actual.getAuth().isBlank());
        assertFalse(actual.getPayload().isBlank());
        assertFalse(actual.getAuth().isBlank());
        assertFalse(actual.getToken().isBlank());
    }


    @Test
    public void testVerifyLogin() {
        var token = getToken();

        var isTokenValid = verifyToken(token);

        assertTrue(isTokenValid);
    }

    private Token getToken() {
        return testRestTemplate.getForEntity(MessageFormat.format("http://localhost:{0}/login/generate", port), Token.class).getBody();
    }

    private Boolean verifyToken(Token token) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return testRestTemplate
                .postForEntity(
                        MessageFormat.format("http://localhost:{0}/login/verify", port),
                        token,
                        Boolean.class,
                        headers
                )
                .getBody();
    }

}