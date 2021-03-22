package com.alicana.btb.bux.platform.api.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Subscription DTO representation.
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
@JsonIgnoreProperties(ignoreUnknown = true)
public final record BuxSubscription(@JsonProperty("subscribeTo") List<String> subscribeTo,
                                    @JsonProperty("unsubscribeFrom") List<String> unsubscribeFrom)
    implements BuxDomainEvent {

}
