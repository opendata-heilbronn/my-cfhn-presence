package de.codeforheilbronn.mycfhn.presence.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PresentPerson {
    private String username;
    private String name;
    private LocalDateTime lastSeen;
    private String location;
}
