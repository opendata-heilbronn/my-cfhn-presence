package de.codeforheilbronn.mycfhn.presence.repository;

import de.codeforheilbronn.mycfhn.presence.model.persistence.PushedPresence;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PushedPresenceRepository extends CrudRepository<PushedPresence, ObjectId> {
    List<PushedPresence> findAll();
}
