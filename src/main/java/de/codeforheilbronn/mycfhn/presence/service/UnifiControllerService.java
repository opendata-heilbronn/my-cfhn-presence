package de.codeforheilbronn.mycfhn.presence.service;

import de.codeforheilbronn.mycfhn.presence.model.unifi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UnifiControllerService {

    public static class ControllerException extends RuntimeException {
        ControllerException(String s) {
            super(s);
        }
    }

    private RestTemplate restTemplate;

    @Value("${cfhn.unifi.url}")
    private String controllerUrl;

    @Value("${cfhn.unifi.user}")
    private String controllerUser;

    @Value("${cfhn.unifi.password}")
    private String controllerPassword;

    @Autowired
    public UnifiControllerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UnifiSession login() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                controllerUrl + "/api/login",
                new LoginData(controllerUser, controllerPassword),
                String.class
        );
        Map<String, String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE)
                .stream()
                .map(line -> line.split("; ")[0])
                .map(cookie -> cookie.split("="))
                .map(cookie -> new Cookie(cookie[0], cookie[1]))
                .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
        if(cookies.containsKey("unifises") && cookies.containsKey("csrf_token")) {
            return new UnifiSession(cookies.get("unifises"), cookies.get("csrf_token"));
        }
        throw new ControllerException("Error logging in");
    }

    public List<UnifiClient> getOnlineClients(UnifiSession session) {
        ResponseEntity<UnifiResponse<List<UnifiClient>>> clients = restTemplate.exchange(
                controllerUrl + "/api/s/default/stat/sta",
                HttpMethod.GET,
                new HttpEntity<>(session.createHeaders()),
                new ParameterizedTypeReference<UnifiResponse<List<UnifiClient>>>() {
                }
        );
        return clients.getBody().getData();
    }
}
