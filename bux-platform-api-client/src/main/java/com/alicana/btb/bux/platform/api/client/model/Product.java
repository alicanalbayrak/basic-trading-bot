package com.alicana.btb.bux.platform.api.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Product Dto.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Product(@JsonProperty("securityId") String securityId,
                      @JsonProperty("symbol") String symbol,
                      @JsonProperty("displayName") String displayName,
                      @JsonProperty("currentPrice") BigMoney currentPrice,
                      @JsonProperty("closingPrice") BigMoney closingPrice) {
}
