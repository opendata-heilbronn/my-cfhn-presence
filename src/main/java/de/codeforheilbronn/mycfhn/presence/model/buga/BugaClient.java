package de.codeforheilbronn.mycfhn.presence.model.buga;

import de.codeforheilbronn.mycfhn.presence.model.UnifiedClient;
import lombok.Value;

@Value
public class BugaClient {
    private long leaseTime;
    private String mac;
    private String ip;

    public UnifiedClient toUnified() {
        return new UnifiedClient(mac, "BUGA");
    }
}
