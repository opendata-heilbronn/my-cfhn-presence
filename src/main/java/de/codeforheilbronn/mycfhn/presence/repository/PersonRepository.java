package de.codeforheilbronn.mycfhn.presence.repository;

import de.codeforheilbronn.mycfhn.presence.model.persistence.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PersonRepository extends MongoRepository<Person, String> {
    Optional<Person> findByMacsContaining(String mac);
}
