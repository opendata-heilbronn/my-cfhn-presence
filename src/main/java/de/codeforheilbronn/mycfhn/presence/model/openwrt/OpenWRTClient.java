package de.codeforheilbronn.mycfhn.presence.model.openwrt;

import de.codeforheilbronn.mycfhn.presence.model.UnifiedClient;
import lombok.Value;

@Value
public class OpenWRTClient {
    private long leaseTime;
    private String mac;
    private String ip;

    public UnifiedClient toUnified(String location) {
        return new UnifiedClient(mac, location);
    }
}
