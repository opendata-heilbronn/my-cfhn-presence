package de.codeforheilbronn.mycfhn.presence.repository;

import de.codeforheilbronn.mycfhn.presence.model.persistence.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends MongoRepository<Person, String> {
    List<Person> findByMacsContaining(String mac);
    Optional<Person> findByUsername(String username);
}
