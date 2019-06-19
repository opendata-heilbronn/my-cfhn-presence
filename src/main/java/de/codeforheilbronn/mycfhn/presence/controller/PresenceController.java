package de.codeforheilbronn.mycfhn.presence.controller;

import de.codeforheilbronn.mycfhn.presence.model.UnifiedClient;
import de.codeforheilbronn.mycfhn.presence.model.api.PresentPerson;
import de.codeforheilbronn.mycfhn.presence.model.openwrt.OpenWRTClient;
import de.codeforheilbronn.mycfhn.presence.model.persistence.Person;
import de.codeforheilbronn.mycfhn.presence.model.persistence.PushedPresence;
import de.codeforheilbronn.mycfhn.presence.model.unifi.UnifiClient;
import de.codeforheilbronn.mycfhn.presence.model.unifi.UnifiSession;
import de.codeforheilbronn.mycfhn.presence.repository.PersonRepository;
import de.codeforheilbronn.mycfhn.presence.service.AuthenticationService;
import de.codeforheilbronn.mycfhn.presence.service.OpenWRTService;
import de.codeforheilbronn.mycfhn.presence.service.PushedPresenceService;
import de.codeforheilbronn.mycfhn.presence.service.UnifiControllerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("presence")
@Slf4j
public class PresenceController {

    private UnifiControllerService controllerService;
    private OpenWRTService bugaService;
    private OpenWRTService mseService;
    private PersonRepository personRepository;
    private AuthenticationService authenticationService;
    private PushedPresenceService pushedPresenceService;

    public PresenceController(
            UnifiControllerService controllerService,
            PersonRepository personRepository,
            AuthenticationService authenticationService,
            OpenWRTService bugaService,
            OpenWRTService mseService,
            PushedPresenceService pushedPresenceService) {
        this.controllerService = controllerService;
        this.personRepository = personRepository;
        this.authenticationService = authenticationService;
        this.bugaService = bugaService;
        this.mseService = mseService;
        this.pushedPresenceService = pushedPresenceService;
    }

    @GetMapping("/")
    public List<PresentPerson> getPresentPersons() throws UnsupportedEncodingException {

        authenticationService.ensureAuthenticated();

        List<UnifiClient> unifiClients;
        try {
            UnifiSession session = controllerService.login();
            unifiClients = controllerService.getOnlineClients(session);

        } catch (Exception e) {
            log.warn(e.getMessage());
            unifiClients = Collections.emptyList();
        }

        List<OpenWRTClient> bugaClients;
        try {
            bugaClients = bugaService.getClients();
        } catch (Exception e) {
            log.warn(e.getMessage());
            bugaClients = Collections.emptyList();
        }

        List<OpenWRTClient> mseClients;
        try {
            mseClients = mseService.getClients();
        } catch (Exception e) {
            log.warn(e.getMessage());
            mseClients = Collections.emptyList();
        }

        List<PushedPresence> pushedClients;
        try {
            pushedClients = pushedPresenceService.getPresences();
        } catch (Exception e) {
            log.warn(e.getMessage());
            pushedClients = Collections.emptyList();
        }

        return concat(
                unifiClients.stream().map(UnifiClient::toUnified),
                bugaClients.stream().map(client -> client.toUnified("BUGA")),
                mseClients.stream().map(client -> client.toUnified("MSE")),
                pushedClients.stream().map(PushedPresence::toUnified)
        ).map(client -> {
            Optional<Person> person = personRepository.findByMacsContaining(client.getMac());
            return person.map(person1 -> new PresentPerson(
                    person1.getUsername(),
                    person1.getName(),
                    LocalDateTime.now(),
                    client.getLocation()
            )).orElse(null);
        }).filter(Objects::nonNull)
                .filter(distinctByKey(person -> person.getUsername() + person.getLocation())).collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @SafeVarargs
    private final Stream<UnifiedClient> concat(Stream<UnifiedClient> ...streams) {
        return Stream.of(streams).reduce(Stream::concat).orElseGet(Stream::empty);
    }
}
