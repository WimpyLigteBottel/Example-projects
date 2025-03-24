package nel.marco.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;

@RestController
public class AuthenticationRestEndpoint {

    private final Logger log = LoggerFactory.getLogger(AuthenticationRestEndpoint.class);

    @Value("${super.duper.secret.password.salt}")
    private String value;

    @GetMapping("/login/generate")
    public ResponseEntity<Token> defaultLogin(@RequestParam(required = false) String accessType) {

        AccessType accessPermission = AccessType.NONE;
        if (accessType != null) {
            accessPermission =
                    Arrays.stream(AccessType.values())
                            .filter(accessType1 -> accessType1.name().equalsIgnoreCase(accessType))
                            .findFirst()
                            .orElse(AccessType.READ);
        }

        String token = JWT.create()
                .withIssuedAt(new Date())
                .withIssuer("MarcoJWTExample")
                .withExpiresAt(nextExpiryDate(20))
                .withClaim("access", accessPermission.name())
                .sign(Algorithm.HMAC512(value));

        DecodedJWT decode = JWT.decode(token);

        return ResponseEntity.ok(new Token(decode.getHeader(), decode.getPayload(), decode.getSignature()));
    }

    @PostMapping("/login/verify")
    public ResponseEntity<Boolean> defaultLogin(@RequestBody @Validated Token token) {
        try {

            String authorization = token.getToken();

            Claim exp = JWT.decode(authorization).getClaim("exp");

            Instant now = Instant.now();
            Instant expiryTime = new Date(exp.asLong() * 1000).toInstant();

            if (now.isAfter(expiryTime)) {
                log.info("Token is expired");
            } else {
                log.info("Token is expiring in  [total={}s]\n", Duration.between(now, expiryTime).toSeconds());
            }
            verifyLogin(authorization);

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Boolean.FALSE);
        }
    }

    public DecodedJWT verifyLogin(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        authorization = authorization.substring("Bearer ".length());
        JWTVerifier secret = JWT.require(Algorithm.HMAC512(value)).build();
        return secret.verify(authorization);
    }

    public DecodedJWT verifyLogin(String token) {
        JWTVerifier secret = JWT.require(Algorithm.HMAC512(value)).build();
       return  secret.verify(token); // throws exception if token is invalid
    }

    public Date nextExpiryDate(long seconds) {
        var nextExpireTime = LocalDateTime.now().plusSeconds(seconds);
        var nextExpiryDate =
                Date.from(
                        nextExpireTime.toInstant(
                                ZoneOffset.systemDefault().getRules().getOffset(Instant.now())
                        )
                );

        return nextExpiryDate;
    }
}
