package com.alicana.btb.bux.platform.api.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents source.
 */
public record Source(@JsonProperty("sourceType") String sourceType,
                     @JsonProperty("sourceId") String sourceId) {
}
