package de.codeforheilbronn.mycfhn.presence.service;

import de.codeforheilbronn.mycfhn.presence.model.api.PushedPresenceModel;
import de.codeforheilbronn.mycfhn.presence.model.persistence.PushedPresence;
import de.codeforheilbronn.mycfhn.presence.repository.PushClientRepository;
import de.codeforheilbronn.mycfhn.presence.repository.PushedPresenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PushedPresenceService {
    private PushClientRepository pushClientRepository;
    private PushedPresenceRepository pushedPresenceRepository;

    public PushedPresenceService(PushClientRepository pushClientRepository, PushedPresenceRepository pushedPresenceRepository) {
        this.pushClientRepository = pushClientRepository;
        this.pushedPresenceRepository = pushedPresenceRepository;
    }

    public List<PushedPresence> addPresences(String token, List<PushedPresenceModel> presences) {
        return pushClientRepository
                .findByToken(token)
                .map(client -> presences.stream()
                        .map(presence -> new PushedPresence(presence.getMac(), presence.getLocation(), client.getName()))
                        .map(pushedPresenceRepository::save)
                        .collect(Collectors.toList())
                ).orElseThrow(() -> new PushClientAuthenticationException("PushClient token not found"));
    }

    public List<PushedPresence> getPresences() {
        return pushedPresenceRepository.findAll();
    }

    public static class PushClientAuthenticationException extends RuntimeException {
        PushClientAuthenticationException(String reason) {
            super(reason);
        }
    }
}
