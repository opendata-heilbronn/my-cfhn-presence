package de.codeforheilbronn.mycfhn.presence.model.unifi;

import lombok.Value;
import org.springframework.http.HttpHeaders;

@Value
public class UnifiSession {
    private String sessionCookie;
    private String csrfCookie;

    public HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "unifises=" + sessionCookie + "; csrf_token=" + csrfCookie);
        return headers;
    }
}
