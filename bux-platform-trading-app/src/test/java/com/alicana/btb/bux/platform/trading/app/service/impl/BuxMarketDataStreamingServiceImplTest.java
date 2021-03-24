package com.alicana.btb.bux.platform.trading.app.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.alicana.btb.bux.platform.api.client.BuxApiWebSocketClient;
import com.alicana.btb.bux.platform.api.client.model.BuxSubscription;
import com.alicana.btb.bux.platform.api.client.model.ConnectedEvent;
import com.alicana.btb.bux.platform.api.client.model.FailureEvent;
import com.alicana.btb.bux.platform.api.client.model.QuoteEvent;
import com.alicana.btb.bux.platform.api.client.model.UnknownEvent;
import com.alicana.btb.bux.platform.trading.app.service.MarketDataStreamingService;
import com.alicana.btb.bux.platform.trading.app.service.TradeStrategy;
import io.reactivex.rxjava3.core.Flowable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class BuxMarketDataStreamingServiceImplTest {

  private TradeStrategy tradeStrategyMock;
  private BuxApiWebSocketClient buxWebSocketClientMock;
  private MarketDataStreamingService marketDataStreamingService;

  @BeforeEach
  void setUp() {
    this.tradeStrategyMock = mock(TradeStrategy.class);
    this.buxWebSocketClientMock = mock(BuxApiWebSocketClient.class);
    this.marketDataStreamingService =
        new BuxMarketDataStreamingServiceImpl(this.buxWebSocketClientMock,
            this.tradeStrategyMock);
  }

  @Test
  void shouldHandleConnectedEventSuccessfully() throws ExecutionException, InterruptedException {

    // GIVEN
    String productId = "productId";
    ConnectedEvent event = new ConnectedEvent("user_id", "session_id", null);

    // WHEN
    when(buxWebSocketClientMock.observeMessages()).thenReturn(Flowable.just(event));
    doAnswer(invocationOnMock -> {
      marketDataStreamingService.stopMarketDataStream();
      return null;
    }).when(buxWebSocketClientMock).sendMessage(anyString());

    CompletableFuture<Void> completableFuture =
        marketDataStreamingService.startMarketDataStream(productId);

    completableFuture.get();

    // THEN
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(buxWebSocketClientMock, atMostOnce()).sendMessage(captor.capture());
    assertThat(captor.getValue()).containsIgnoringCase("trading.product.productId");

  }

  @Test
  void shouldStopStreaming_whenCannotSerializeBuxSubscription()
      throws ExecutionException, InterruptedException {

    // GIVEN
    String productId = "productId";
    ConnectedEvent event = new ConnectedEvent("user_id", "session_id", null);
    this.marketDataStreamingService =
        spy(new BuxMarketDataStreamingServiceImpl(this.buxWebSocketClientMock,
            this.tradeStrategyMock));
    // WHEN
    when(buxWebSocketClientMock.observeMessages()).thenReturn(Flowable.just(event));
    doThrow(RuntimeException.class).when(buxWebSocketClientMock).sendMessage(anyString());

    CompletableFuture<Void> completableFuture =
        marketDataStreamingService.startMarketDataStream(productId);
    completableFuture.get();

    // THEN
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(buxWebSocketClientMock, atMostOnce()).sendMessage(captor.capture());
    verify(marketDataStreamingService, atMostOnce()).stopMarketDataStream();
  }


  @Test
  void shouldHandleEveryBuxDomainEvent() throws ExecutionException, InterruptedException {

    // GIVEN
    String productId = "productId";
    ConnectedEvent connectedEvent = new ConnectedEvent("user_id", "session_id", null);
    BuxSubscription buxSubscription = mock(BuxSubscription.class);
    QuoteEvent quoteEvent = new QuoteEvent("sec-id", "14.20", null);
    FailureEvent failureEvent = mock(FailureEvent.class);
    UnknownEvent unknownEvent = mock(UnknownEvent.class);

    // WHEN
    when(buxWebSocketClientMock.observeMessages()).thenReturn(
        Flowable.just(connectedEvent, buxSubscription, quoteEvent, failureEvent, unknownEvent,
            quoteEvent));
    when(this.tradeStrategyMock.isFinished()).thenReturn(false, true);
    this.marketDataStreamingService =
        spy(new BuxMarketDataStreamingServiceImpl(this.buxWebSocketClientMock,
            this.tradeStrategyMock));

    CompletableFuture<Void> completableFuture =
        marketDataStreamingService.startMarketDataStream(productId);
    completableFuture.get();
    // THEN
    verify(this.tradeStrategyMock, times(2)).isFinished();
    verify(this.tradeStrategyMock, atMostOnce()).onQuoteEvent(any());
  }

  @Test
  void shouldStopStreamin_whenExecutionErrorOccurs()
      throws ExecutionException, InterruptedException {

    // GIVEN
    QuoteEvent event = mock(QuoteEvent.class);
    this.marketDataStreamingService =
        spy(new BuxMarketDataStreamingServiceImpl(this.buxWebSocketClientMock,
            this.tradeStrategyMock));

    // WHEN
    when(buxWebSocketClientMock.observeMessages()).thenReturn(Flowable.just(event));
    doThrow(RuntimeException.class).when(tradeStrategyMock).isFinished();

    CompletableFuture<Void> completableFuture =
        marketDataStreamingService.startMarketDataStream("productId");

    completableFuture.get();

    // THEN
    verify(marketDataStreamingService, times(1)).stopMarketDataStream();
  }
}