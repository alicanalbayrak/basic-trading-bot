package com.alicana.btb.bux.platform.trading.app.service.impl;

import com.alicana.btb.bux.platform.api.client.model.QuoteEvent;
import com.alicana.btb.bux.platform.api.client.model.Trade;
import com.alicana.btb.bux.platform.trading.app.model.TradingBotInputParams;
import com.alicana.btb.bux.platform.trading.app.service.TradeService;
import com.alicana.btb.bux.platform.trading.app.service.TradeStrategy;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simply tries to have a profitable trade by buying a product at given rate,
 * and selling at predefined rate. This strategy sells product if the value decreases
 * below given threshold (lower limit sell)
 */
public class SimpleTradeStrategy implements TradeStrategy {

  private static final Logger log = LoggerFactory.getLogger(SimpleTradeStrategy.class);

  private final AtomicBoolean isSequenceExecuted = new AtomicBoolean(false);

  private final TradingBotInputParams tradingBotInputParams;
  private final TradeService tradeService;

  private String positionId;

  /**
   * C'tor.
   *
   * @param tradingBotInputParams User provided input.
   * @param tradeService          The Trade service
   */
  public SimpleTradeStrategy(TradingBotInputParams tradingBotInputParams,
                             TradeService tradeService) {
    this.tradingBotInputParams = tradingBotInputParams;
    this.tradeService = tradeService;
  }

  @Override
  public void onQuoteEvent(QuoteEvent quoteEvent) {

    if (isSequenceExecuted.get()) {
      log.warn("Trade sequence has been already executed. Ignoring quote event...");
      return;
    }

    BigDecimal currentPrice = new BigDecimal(quoteEvent.currentPrice());

    if (Optional.ofNullable(positionId).isEmpty()) {

      BigDecimal buyPrice = tradingBotInputParams.buyPrice();
      // TODO CLARIFY !
      //  not sure if should be exactly same price ?
      if (currentPrice.compareTo(buyPrice) <= 0) {
        this.positionId = tradeService.openPosition(tradingBotInputParams.productId(), buyPrice);
      }
    } else {

      BigDecimal lowerSellLimit = tradingBotInputParams.lowerSellLimit();
      BigDecimal upperSellLimit = tradingBotInputParams.upperSellLimit();

      if (currentPrice.compareTo(lowerSellLimit) <= 0
          || currentPrice.compareTo(upperSellLimit) >= 0) {

        log.info("Price hit! Closing position. Current={}, Lower={}, Upper={}", currentPrice,
            lowerSellLimit, upperSellLimit);
        Trade tradeResult = tradeService.closePosition(this.positionId);
        log.info(tradeResult.toString());
        this.isSequenceExecuted.set(true);
      }
    }
  }

  @Override
  public boolean isFinished() {
    return this.isSequenceExecuted.get();
  }

}
