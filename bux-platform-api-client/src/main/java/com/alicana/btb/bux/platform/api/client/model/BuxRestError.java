package com.alicana.btb.bux.platform.api.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for deserializing Bux Rest API error response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record BuxRestError(@JsonProperty("message") String message,
                           @JsonProperty("developerMessage") String developerMessage,
                           @JsonProperty("errorCode") String errorCode) {
}