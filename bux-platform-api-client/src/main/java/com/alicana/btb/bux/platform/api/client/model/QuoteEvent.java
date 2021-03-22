package com.alicana.btb.bux.platform.api.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

/**
 * Market ticker event for the tradable instrument (securityId).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final record QuoteEvent(@JsonProperty("securityId") String securityId,
                               @JsonProperty("currentPrice") String currentPrice,
                               @JsonProperty("timeStamp") DateTime timeStamp)
    implements BuxDomainEvent {
}
