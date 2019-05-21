package de.codeforheilbronn.mycfhn.presence.configuration;

import de.codeforheilbronn.mycfhn.presence.service.OpenWRTService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenWRTTargetConfig {
    @Bean
    public OpenWRTService bugaService(RestTemplate restTemplate) {
        return new OpenWRTService(restTemplate, "192.168.11.1");
    }

    @Bean
    public OpenWRTService mseService(RestTemplate restTemplate) {
        return new OpenWRTService(restTemplate,"192.168.12.1");
    }
}
