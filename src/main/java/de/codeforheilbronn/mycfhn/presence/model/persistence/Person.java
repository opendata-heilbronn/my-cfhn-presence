package de.codeforheilbronn.mycfhn.presence.model.persistence;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Person {
    @Indexed(unique = true)
    private String username;
    private String name;
    private List<String> macs;
}
