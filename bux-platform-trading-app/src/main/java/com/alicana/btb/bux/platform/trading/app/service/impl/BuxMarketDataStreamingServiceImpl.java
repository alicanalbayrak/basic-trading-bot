package com.alicana.btb.bux.platform.trading.app.service.impl;

import com.alicana.btb.bux.platform.api.client.BuxApiWebSocketClient;
import com.alicana.btb.bux.platform.api.client.model.BuxDomainEvent;
import com.alicana.btb.bux.platform.api.client.model.BuxSubscription;
import com.alicana.btb.bux.platform.api.client.model.ConnectedEvent;
import com.alicana.btb.bux.platform.api.client.model.FailureEvent;
import com.alicana.btb.bux.platform.api.client.model.QuoteEvent;
import com.alicana.btb.bux.platform.api.client.model.UnknownEvent;
import com.alicana.btb.bux.platform.trading.app.service.MarketDataStreamingService;
import com.alicana.btb.bux.platform.trading.app.service.TradeStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bux platform market data streaming service implementation.
 *
 * @see MarketDataStreamingService
 */
public class BuxMarketDataStreamingServiceImpl implements MarketDataStreamingService {

  private static final Logger log =
      LoggerFactory.getLogger(BuxMarketDataStreamingServiceImpl.class);
  private static final String subscriptionProductTemplate = "trading.product.%s";

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final BuxApiWebSocketClient buxApiWebSocketClient;
  private final TradeStrategy tradeStrategy;

  private String productId;
  private Disposable webSocketClientDisposable;
  private CompletableFuture<Void> completableFuture;


  public BuxMarketDataStreamingServiceImpl(
      BuxApiWebSocketClient buxApiWebSocketClient,
      TradeStrategy tradeStrategy) {
    this.buxApiWebSocketClient = buxApiWebSocketClient;
    this.tradeStrategy = tradeStrategy;
  }

  @Override
  public CompletableFuture<Void> startMarketDataStream(final String productId) {

    this.productId = productId;
    this.webSocketClientDisposable = buxApiWebSocketClient.observeMessages()
        .observeOn(Schedulers.single())
        .subscribe(this::handleBuxDomainEvent,
            throwable -> {
              log.error("An error occurred while processing events", throwable);
              stopMarketDataStream();
            });

    this.completableFuture = new CompletableFuture<>();
    return completableFuture;
  }

  @Override
  public void stopMarketDataStream() {
    if (webSocketClientDisposable != null && !webSocketClientDisposable.isDisposed()) {
      webSocketClientDisposable.dispose();
    }

    this.completableFuture.complete(null);
  }

  private void handleBuxDomainEvent(final BuxDomainEvent event) {

    if (event instanceof ConnectedEvent ce) {
      handleConnectedEvent(ce);
    } else if (event instanceof BuxSubscription bs) {
      handleBuxSubscription(bs);
    } else if (event instanceof QuoteEvent qe) {
      handleQuoteEvent(qe);
    } else if (event instanceof FailureEvent fe) {
      handleFailureEvent(fe);
    } else if (event instanceof UnknownEvent ue) {
      log.error("Received unknown event!. UnknownEvent={}", ue.eventAsString());
    }

  }

  private void handleConnectedEvent(final ConnectedEvent connectedEvent) {
    log.info(connectedEvent.toString());
    List<String> subscribeTo =
        Collections.singletonList(String.format(subscriptionProductTemplate, this.productId));
    BuxSubscription subs = new BuxSubscription(subscribeTo, Collections.emptyList());

    try {
      buxApiWebSocketClient.sendMessage(objectMapper.writeValueAsString(subs));
    } catch (Exception e) {
      log.error("An exception occurred while serializing bux subscription.", e);
      stopMarketDataStream();
    }
  }

  private void handleFailureEvent(FailureEvent failureEvent) {
    log.warn(failureEvent.toString());
  }

  private void handleQuoteEvent(final QuoteEvent quoteEvent) {

    if (tradeStrategy.isFinished()) {
      log.info("Trade execution finished. Stopping market data stream.");
      stopMarketDataStream();
      return;
    }

    log.info(quoteEvent.toString());
    tradeStrategy.onQuoteEvent(quoteEvent);
  }

  private void handleBuxSubscription(final BuxSubscription buxSubscription) {
    log.info(buxSubscription.toString());
  }


}
