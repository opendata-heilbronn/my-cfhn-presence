package de.codeforheilbronn.mycfhn.presence.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushedPresenceModel {
    private String mac;
    private String location;
}
