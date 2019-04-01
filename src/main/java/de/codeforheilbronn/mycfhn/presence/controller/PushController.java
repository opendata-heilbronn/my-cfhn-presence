package de.codeforheilbronn.mycfhn.presence.controller;

import de.codeforheilbronn.mycfhn.presence.model.api.PushedPresenceModel;
import de.codeforheilbronn.mycfhn.presence.model.persistence.PushedPresence;
import de.codeforheilbronn.mycfhn.presence.service.PushedPresenceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("push")
public class PushController {

    private PushedPresenceService pushedPresenceService;

    public PushController(PushedPresenceService pushedPresenceService) {
        this.pushedPresenceService = pushedPresenceService;
    }

    @PostMapping
    public List<PushedPresence> pushDiscoveredMacs(
            @RequestParam String token,
            @RequestBody List<PushedPresenceModel> pushedPresenceModels
    ) {
        return pushedPresenceService.addPresences(token, pushedPresenceModels);
    }
}
