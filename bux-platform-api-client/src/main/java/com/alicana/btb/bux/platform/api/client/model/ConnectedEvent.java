package com.alicana.btb.bux.platform.api.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

/**
 * Connected event body.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final record ConnectedEvent(@JsonProperty("userId") String userId,
                                   @JsonProperty("sessionId") String sessionId,
                                   @JsonProperty("time") DateTime dt)
    implements BuxDomainEvent {

}
