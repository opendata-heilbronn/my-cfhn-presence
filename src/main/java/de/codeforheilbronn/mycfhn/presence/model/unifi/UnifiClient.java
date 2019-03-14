package de.codeforheilbronn.mycfhn.presence.model.unifi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.codeforheilbronn.mycfhn.presence.model.UnifiedClient;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnifiClient {
    @JsonProperty("_last_seen_by_uap")
    private int lastSeenAP;
    @JsonProperty("_last_seen_by_ugw")
    private int lastSeenGateway;

    private String mac;

    public UnifiedClient toUnified() {
        return new UnifiedClient(mac, "Cowo");
    }
}
