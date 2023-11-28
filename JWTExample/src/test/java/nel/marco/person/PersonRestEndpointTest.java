package nel.marco.person;

import nel.marco.auth.AccessType;
import nel.marco.auth.Token;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class PersonRestEndpointTest {

    public TestRestTemplate template = new TestRestTemplate();

    @LocalServerPort
    public String port;

    @Test
    public void getPerson_tokenIsInvalid_expect401() {
        String url = MessageFormat.format("http://localhost:{0}/person", port);


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("token");
        headers.setContentType(MediaType.APPLICATION_JSON);
        var entity = new HttpEntity<>(null, headers);

        var forEntity = template.exchange(url, HttpMethod.GET, entity, String.class);

        assertTrue(forEntity.getStatusCode().is4xxClientError());
    }


    @Test
    public void getPerson_tokenIsvalid_expect200() {
        String url = MessageFormat.format("http://localhost:{0}/person", port);
        HttpEntity<Object> entity = setupHttpEntity(null,AccessType.READ_WRITE);

        var forEntity = template.exchange(url, HttpMethod.GET, entity, Object.class);

        assertTrue(forEntity.getStatusCode().is2xxSuccessful());
    }


    @Test
    public void createPerson_tokenIsvalid_expect200() {
        String url = MessageFormat.format("http://localhost:{0}/person", port);

        var person = new Person();
        person.setAge(12);
        person.setName("Marco");
        person.setSurname("SURNAME");

        HttpEntity<Object> entity = setupHttpEntity(person, AccessType.READ_WRITE);

        var forEntity = template.exchange(url, HttpMethod.POST, entity, Object.class);

        assertTrue(forEntity.getStatusCode().is2xxSuccessful());
    }


    private HttpEntity<Object> setupHttpEntity(Object request, AccessType accessType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", getToken(accessType).getToken());
        headers.setBearerAuth(getToken(accessType).getToken());
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        return entity;
    }


    private Token getToken(AccessType accessType) {
        String url = MessageFormat.format("http://localhost:{0}/login/generate?accessType={1}", port, accessType);
        return template.getForEntity(url, Token.class).getBody();
    }


}