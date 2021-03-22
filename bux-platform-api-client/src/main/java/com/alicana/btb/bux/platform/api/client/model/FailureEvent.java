package com.alicana.btb.bux.platform.api.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Error message representation that are sent from BUX backend.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final record FailureEvent(@JsonProperty("message") String message,
                                 @JsonProperty("developerMessage") String developerMessage,
                                 @JsonProperty("errorCode") String errorCode)
    implements BuxDomainEvent {
}
