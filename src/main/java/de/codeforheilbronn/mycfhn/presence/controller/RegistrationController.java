package de.codeforheilbronn.mycfhn.presence.controller;

import de.codeforheilbronn.mycfhn.presence.model.auth.TokenData;
import de.codeforheilbronn.mycfhn.presence.model.persistence.Person;
import de.codeforheilbronn.mycfhn.presence.repository.PersonRepository;
import de.codeforheilbronn.mycfhn.presence.service.AuthenticationService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("registration")
@Profile("cfhnAuth")
public class RegistrationController {

    private AuthenticationService authenticationService;
    private PersonRepository personRepository;

    public RegistrationController(AuthenticationService authenticationService, PersonRepository personRepository) {
        this.authenticationService = authenticationService;
        this.personRepository = personRepository;
    }

    @GetMapping("/me")
    public List<String> getMacs() throws UnsupportedEncodingException {
        authenticationService.ensureAuthenticated();
        return authenticationService.getUserData()
                .flatMap(tokenData -> personRepository.findByUsername(tokenData.getUsername()))
                .map(Person::getMacs)
                .orElse(Collections.emptyList());
    }

    @PutMapping("/me")
    public List<String> setMacs(
            @RequestBody List<String> macs
    ) throws UnsupportedEncodingException {
        authenticationService.ensureAuthenticated();
        return authenticationService.getUserData()
                .map(this::findOrNew)
                .map(member -> member.setMacs(macs))
                .map(personRepository::save)
                .map(Person::getMacs)
                .orElse(Collections.emptyList());
    }

    private Person findOrNew(TokenData tokenData) {
        return personRepository.findByUsername(tokenData.getUsername())
                .orElse(new Person(tokenData.getUsername(), tokenData.getName(), Collections.emptyList()));
    }
}
