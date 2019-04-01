package de.codeforheilbronn.mycfhn.presence.repository;

import de.codeforheilbronn.mycfhn.presence.model.persistence.PushClient;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PushClientRepository extends CrudRepository<PushClient, ObjectId> {
    Optional<PushClient> findByName(String name);
    Optional<PushClient> findByToken(String token);
}
