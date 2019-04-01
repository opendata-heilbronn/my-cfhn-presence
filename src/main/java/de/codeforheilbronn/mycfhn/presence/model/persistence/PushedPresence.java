package de.codeforheilbronn.mycfhn.presence.model.persistence;

import de.codeforheilbronn.mycfhn.presence.model.UnifiedClient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class PushedPresence {
    @Id
    private ObjectId id;
    @NonNull
    private String mac;
    @Indexed(expireAfterSeconds = 5 * 60)
    private Date time = new Date();
    @NonNull
    private String location;
    @NonNull
    private String byClient;

    public UnifiedClient toUnified() {
        return new UnifiedClient(mac, location);
    }
}
