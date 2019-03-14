package de.codeforheilbronn.mycfhn.presence.model;

import lombok.Value;

@Value
public class UnifiedClient {
    private String mac;
    private String location;
}
