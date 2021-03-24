package com.alicana.btb.bux.platform.trading.app.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.alicana.btb.bux.platform.api.client.model.QuoteEvent;
import com.alicana.btb.bux.platform.api.client.model.Trade;
import com.alicana.btb.bux.platform.trading.app.model.TradingBotInputParams;
import com.alicana.btb.bux.platform.trading.app.service.TradeService;
import com.alicana.btb.bux.platform.trading.app.service.TradeStrategy;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class SimpleTradeStrategyTest {


  @Test
  void shouldSuccessfullyExecuteSequence() {

    // GIVEN
    TradeService tradeService = mock(TradeService.class);
    BigDecimal buyPrice = new BigDecimal("14608.0");
    BigDecimal upperSellLimit = new BigDecimal("14610.0");
    BigDecimal lowerSellLimit = new BigDecimal("14606.5");
    String positionId = "1234-5678-9101-2131-4567";
    String productId = "sbXXX";

    Trade mockTrade = mock(Trade.class);
    TradingBotInputParams tradingBotInputParams =
        new TradingBotInputParams(productId, buyPrice, upperSellLimit, lowerSellLimit);
    TradeStrategy tradeStrategy =
        new SimpleTradeStrategy(tradingBotInputParams, tradeService);

    // WHEN
    when(tradeService.openPosition(productId, buyPrice)).thenReturn(positionId);
    when(tradeService.closePosition(anyString())).thenReturn(mockTrade);

    // 10 market ticks until buy option
    BigDecimal currentPrice = new BigDecimal("14612.5");
    BigDecimal step = new BigDecimal("0.5");
    for (int i = 0; i < 10; i++) {
      tradeStrategy.onQuoteEvent(new QuoteEvent("sbXXX", currentPrice.toString(), null));
      currentPrice = currentPrice.subtract(step);
    }

    // Requires yet another 4 market ticks until upper sell option
    for (int i = 0; i < 10; i++) {
      tradeStrategy.onQuoteEvent(new QuoteEvent("sbXXX", currentPrice.toString(), null));
      currentPrice = currentPrice.add(step);
      if (tradeStrategy.isFinished()) {
        break;
      }
    }

    // THEN
    verify(tradeService, atMostOnce()).openPosition(productId, buyPrice);
    verify(tradeService, atMostOnce()).closePosition(positionId);
    assertThat(tradeStrategy.isFinished()).isTrue();
  }
}