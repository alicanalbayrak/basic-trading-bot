package com.alicana.btb.bux.platform.trading.app.service;

import java.util.concurrent.CompletableFuture;

/**
 * Subscribes given product and feeds the trade strategy component.
 */
public interface MarketDataStreamingService {

  CompletableFuture<Void> startMarketDataStream(final String productId);

  void stopMarketDataStream();

}
