package de.codeforheilbronn.mycfhn.presence.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class TokenData {
    private String username;
    private String name;
    private List<String> groups;
}
