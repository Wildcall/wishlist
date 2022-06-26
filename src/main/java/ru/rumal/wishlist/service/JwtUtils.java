package ru.rumal.wishlist.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.model.entity.User;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@Service
public class JwtUtils {

    private final String eventIdClaimName = "eventId";
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public JwtUtils(@Value("${secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtVerifier = JWT
                .require(algorithm)
                .build();
    }

    public String generateReservedToken(@NotNull String userId,
                                        @NotNull Event event) {
        return JWT
                .create()
                .withSubject(userId)
                .withClaim(eventIdClaimName, event.getId())
                .withExpiresAt(Date.from(event
                                                 .getDate()
                                                 .atZone(ZoneId.systemDefault())
                                                 .toInstant()))
                .sign(algorithm);
    }

    public Optional<Event> verifyReservedToken(@NotNull String token) {
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            Event event = new Event();
            event.setId(decodedJWT
                                .getClaim(eventIdClaimName)
                                .asLong());
            event.setUser(new User(decodedJWT.getSubject()));
            return Optional.of(event);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
