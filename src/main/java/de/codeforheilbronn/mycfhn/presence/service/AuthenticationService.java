package de.codeforheilbronn.mycfhn.presence.service;

import de.codeforheilbronn.mycfhn.presence.model.auth.TokenData;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Component
public interface AuthenticationService {
    boolean isAuthenticated();

    Optional<TokenData> getUserData();

    void ensureAuthenticated() throws UnsupportedEncodingException;
}
