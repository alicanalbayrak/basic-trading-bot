package com.alicana.btb.bux.platform.trading.app.service;

import com.alicana.btb.bux.platform.api.client.model.QuoteEvent;

/**
 * Strategy interface to decide on trade signal.
 */
public interface TradeStrategy {

  void onQuoteEvent(QuoteEvent quoteEvent);

  boolean isFinished();

}
