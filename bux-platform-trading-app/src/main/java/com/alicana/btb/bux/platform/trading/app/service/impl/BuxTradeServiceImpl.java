package com.alicana.btb.bux.platform.trading.app.service.impl;

import com.alicana.btb.bux.platform.api.client.BuxApiRestClient;
import com.alicana.btb.bux.platform.api.client.model.BigMoney;
import com.alicana.btb.bux.platform.api.client.model.OpenPosition;
import com.alicana.btb.bux.platform.api.client.model.Source;
import com.alicana.btb.bux.platform.api.client.model.Trade;
import com.alicana.btb.bux.platform.api.client.model.TradeDirection;
import com.alicana.btb.bux.platform.trading.app.service.TradeService;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Delegates open/close position calls to the Bux API
 */
public class BuxTradeServiceImpl implements TradeService {

  private static final Logger log = LoggerFactory.getLogger(BuxTradeServiceImpl.class);

  private final BuxApiRestClient buxApiRestClient;

  public BuxTradeServiceImpl(BuxApiRestClient buxApiRestClient) {
    this.buxApiRestClient = buxApiRestClient;
  }

  @Override
  public String openPosition(String productId, BigDecimal buyPrice) {

    BigMoney investingAmount = new BigMoney("BUX", buyPrice.scale(), buyPrice);
    Source source = new Source("OTHER", null);
    OpenPosition openPos =
        new OpenPosition(productId, investingAmount, 1, TradeDirection.BUY, source);

    Trade result = buxApiRestClient.openPosition(openPos);

    log.info("Open position result: {}", result);
    return result.positionId();
  }

  @Override
  public Trade closePosition(String positionId) {

    Trade result = buxApiRestClient.closePosition(positionId);
    log.info("Close position result: {}", result.profitAndLoss());
    return result;
  }

}
