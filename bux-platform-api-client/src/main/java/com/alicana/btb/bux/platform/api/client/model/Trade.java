package com.alicana.btb.bux.platform.api.client.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Trade Dto.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Trade(@JsonProperty("id") String id,
                    @JsonProperty("positionId") String positionId,
                    @JsonProperty("profitAndLoss") BigMoney profitAndLoss,
                    @JsonProperty("product") Product product,
                    @JsonProperty("investingAmount") BigMoney investingAmount,
                    @JsonProperty("price") BigMoney price,
                    @JsonProperty("leverage") Integer leverage,
                    @JsonProperty("direction") TradeDirection direction,
                    @JsonProperty("type") TradeType type) {
}