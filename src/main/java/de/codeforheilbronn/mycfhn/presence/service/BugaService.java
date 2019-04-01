package de.codeforheilbronn.mycfhn.presence.service;

import de.codeforheilbronn.mycfhn.presence.model.buga.BugaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BugaService {

    private RestTemplate restTemplate;

    public BugaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BugaClient> getClients() {
        try {
            String contents = restTemplate.getForObject("http://192.168.11.1/dhcp.txt", String.class);

            if (contents == null) {
                return Collections.emptyList();
            }

            return Arrays.stream(contents.split("\n"))
                    .map(line -> line.split(" "))
                    .map(parts -> new BugaClient(Long.parseLong(parts[0]), parts[1], parts[2]))
                    .collect(Collectors.toList());
        } catch (RestClientException e) {
            log.error("Could not access BUGA DHCP leases", e);
            return Collections.emptyList();
        }
    }
}
