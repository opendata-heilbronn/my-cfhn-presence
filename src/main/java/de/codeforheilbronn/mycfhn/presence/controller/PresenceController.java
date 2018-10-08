package de.codeforheilbronn.mycfhn.presence.controller;

import de.codeforheilbronn.mycfhn.presence.model.api.PresentPerson;
import de.codeforheilbronn.mycfhn.presence.model.persistence.Person;
import de.codeforheilbronn.mycfhn.presence.model.unifi.UnifiClient;
import de.codeforheilbronn.mycfhn.presence.model.unifi.UnifiSession;
import de.codeforheilbronn.mycfhn.presence.repository.PersonRepository;
import de.codeforheilbronn.mycfhn.presence.service.AuthenticationService;
import de.codeforheilbronn.mycfhn.presence.service.UnifiControllerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("presence")
public class PresenceController {

    private UnifiControllerService controllerService;
    private PersonRepository personRepository;
    private AuthenticationService authenticationService;

    public PresenceController(UnifiControllerService controllerService, PersonRepository personRepository, AuthenticationService authenticationService) {
        this.controllerService = controllerService;
        this.personRepository = personRepository;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/")
    public List<PresentPerson> getPresentPersons() throws UnsupportedEncodingException {

        authenticationService.ensureAuthenticated();

        UnifiSession session = controllerService.login();
        List<UnifiClient> clients = controllerService.getOnlineClients(session);

        return clients.stream().map(client -> {
            Optional<Person> person = personRepository.findByMacsContaining(client.getMac());
            if (!person.isPresent()) {
                return null;
            }
            int lastSeen = Math.max(client.getLastSeenAP(), client.getLastSeenGateway());
            return new PresentPerson(
                    person.get().getUsername(),
                    person.get().getName(),
                    LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(lastSeen), TimeZone.getDefault().toZoneId()
                    )
            );
        }).filter(Objects::nonNull)
                .filter(distinctByKey(PresentPerson::getUsername)).collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
