package de.codeforheilbronn.mycfhn.presence.service;

import de.codeforheilbronn.mycfhn.presence.model.auth.TokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@Profile("cfhnAuth")
public class TokenValidationService {

    private PublicKey jwtPublicKey;

    public TokenValidationService(@Value("${cfhn.presence.auth.publicKeyUrl}") String tokenUrl, RestTemplate template) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String result = template.getForObject(tokenUrl, String.class);
        if (result == null) {
            throw new RuntimeException("Could not get JWT public Key from login service");
        }
        String privateKeyString = result
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "");

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        jwtPublicKey = keyFactory.generatePublic(
                new X509EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
        );
    }

    public Optional<TokenData> validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtPublicKey).parseClaimsJws(token);
            return Optional.of(TokenData.builder()
                    .username(claims.getBody().getSubject())
                    .groups(claims.getBody().get("groups", List.class))
                    .build());
        } catch (JwtException e) {
            log.warn("Invalid JWT", e);
        }
        return Optional.empty();
    }
}
