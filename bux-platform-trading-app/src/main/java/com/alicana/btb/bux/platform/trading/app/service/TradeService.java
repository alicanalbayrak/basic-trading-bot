package com.alicana.btb.bux.platform.trading.app.service;

import com.alicana.btb.bux.platform.api.client.model.Trade;
import java.math.BigDecimal;

/**
 * Provides interface to platform to open and close position.
 */
public interface TradeService {

  String openPosition(final String productId, final BigDecimal buyPrice);

  Trade closePosition(final String positionId);

}
