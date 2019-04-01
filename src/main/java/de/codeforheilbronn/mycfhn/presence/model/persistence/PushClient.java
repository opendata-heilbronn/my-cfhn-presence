package de.codeforheilbronn.mycfhn.presence.model.persistence;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class PushClient {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String name;
    @Indexed(unique = true)
    private String token;
}
