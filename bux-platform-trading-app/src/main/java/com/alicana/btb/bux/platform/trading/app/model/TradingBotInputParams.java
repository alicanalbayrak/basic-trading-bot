package com.alicana.btb.bux.platform.trading.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * This represents the user input at startup.
 */
public record TradingBotInputParams(
    @JsonProperty(value = "productId", required = true) String productId,
    @JsonProperty(value = "buyPrice", required = true) BigDecimal buyPrice,
    @JsonProperty(value = "upperSellLimit", required = true) BigDecimal upperSellLimit,
    @JsonProperty(value = "lowerSellLimit", required = true) BigDecimal lowerSellLimit) {

  /**
   * To have a valida {@link TradingBotInputParams} object, params should satisfy following rules;
   * lower limit sell price < buy price < upper limit sell.
   *
   * @return Validation result in boolean
   */
  public boolean isValid() {

    return buyPrice.compareTo(lowerSellLimit) > 0 && upperSellLimit.compareTo(buyPrice) > 0;
  }

}
