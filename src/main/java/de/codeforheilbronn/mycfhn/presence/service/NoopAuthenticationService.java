package de.codeforheilbronn.mycfhn.presence.service;

import de.codeforheilbronn.mycfhn.presence.model.auth.TokenData;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Profile("!cfhnAuth")
@Component
public class NoopAuthenticationService implements AuthenticationService {
    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Optional<TokenData> getUserData() {
        return Optional.of(TokenData.builder()
                .username("admin")
                .groups(Collections.emptyList())
                .build());
    }

    @Override
    public void ensureAuthenticated() {
        // Noop
    }
}
