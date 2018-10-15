package de.codeforheilbronn.mycfhn.presence.model.persistence;

import lombok.*;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class Person {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private String username;
    @NonNull
    private String name;
    @NonNull
    private List<String> macs;
}
