package de.codeforheilbronn.mycfhn.presence.model.unifi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnifiResponse<T> {
    private T data;
}
