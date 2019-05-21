package de.codeforheilbronn.mycfhn.presence.service;

import de.codeforheilbronn.mycfhn.presence.model.openwrt.OpenWRTClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class OpenWRTService {

    private RestTemplate restTemplate;
    private String address;

    public OpenWRTService(RestTemplate restTemplate, String address) {
        this.restTemplate = restTemplate;
        this.address = address;
    }

    public List<OpenWRTClient> getClients() {
        try {
            String contents = restTemplate.getForObject("http://" + address + "/dhcp.txt", String.class);

            if (contents == null) {
                return Collections.emptyList();
            }

            return Arrays.stream(contents.split("\n"))
                    .map(line -> line.split(" "))
                    .map(parts -> new OpenWRTClient(Long.parseLong(parts[0]), parts[1], parts[2]))
                    .collect(Collectors.toList());
        } catch (RestClientException e) {
            log.error("Could not access BUGA DHCP leases", e);
            return Collections.emptyList();
        }
    }
}
