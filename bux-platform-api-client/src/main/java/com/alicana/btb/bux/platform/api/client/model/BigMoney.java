package com.alicana.btb.bux.platform.api.client.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * Represents investment amount.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BigMoney(@JsonProperty(value = "currency", required = true) String currency,
                       @JsonProperty(value = "decimals", required = true) int decimalPlaces,
                       @JsonProperty(value = "amount", required = true) BigDecimal amount) {

}
