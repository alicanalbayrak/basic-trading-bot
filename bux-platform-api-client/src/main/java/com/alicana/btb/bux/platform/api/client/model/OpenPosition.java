package com.alicana.btb.bux.platform.api.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Open position request payload.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenPosition(
    @JsonProperty(value = "productId", required = true) String productId,
    @JsonProperty(value = "investingAmount", required = true) BigMoney investingAmount,
    @JsonProperty(value = "leverage", required = true) Integer leverage,
    @JsonProperty(value = "direction", required = true) TradeDirection direction,
    @JsonProperty("source") Source source) {
}
