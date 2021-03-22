package com.alicana.btb.bux.platform.api.client;

import com.alicana.btb.bux.platform.api.client.adapter.ReactiveWebSocket;
import com.alicana.btb.bux.platform.api.client.adapter.impl.ReactiveWebSocketImpl;
import com.alicana.btb.bux.platform.api.client.adapter.state.Connected;
import com.alicana.btb.bux.platform.api.client.adapter.state.SendCapable;
import com.alicana.btb.bux.platform.api.client.adapter.state.SocketState;
import com.alicana.btb.bux.platform.api.client.model.BuxDomainEvent;
import com.alicana.btb.bux.platform.api.client.serde.BuxDomainEventDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.processors.PublishProcessor;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Bux API WebSocket client implementation using OkHttp.
 */
public class BuxApiWebSocketClientImpl implements BuxApiWebSocketClient, Closeable {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final PublishProcessor<String> outgoingMessageProcessor = PublishProcessor.create();

  private final OkHttpClient okHttpClient;
  private final String webSocketUrl;
  private ReactiveWebSocket reactiveWebSocket;
  private Flowable<SocketState> socketConnection;

  /**
   * Creates OkHttp implementation of {@link BuxApiWebSocketClient}.
   *
   * @param okHttpClient The OkHttp client
   * @param webSocketUrl The webSocket streaming url
   */
  public BuxApiWebSocketClientImpl(final OkHttpClient okHttpClient, final String webSocketUrl) {
    this.okHttpClient = okHttpClient;
    this.webSocketUrl = webSocketUrl;
    initialize();
  }

  @Override
  public Flowable<SocketState> observeSocketState() {
    return socketConnection;
  }

  @Override
  public Flowable<BuxDomainEvent> observeMessages() {
    return socketConnection.ofType(Connected.class)
        .switchMap(Connected::messageFlowable)
        .map(msg -> objectMapper.readValue(msg, BuxDomainEvent.class));
  }

  @Override
  public void sendMessage(final String msg) {
    outgoingMessageProcessor.onNext(msg);
  }

  /**
   * Initialize WebSocket client with RxJava adapters.
   */
  private void initialize() {

    SimpleModule buxDomainEventSerdeModule = new SimpleModule();
    buxDomainEventSerdeModule
        .addDeserializer(BuxDomainEvent.class, new BuxDomainEventDeserializer());
    objectMapper.registerModule(buxDomainEventSerdeModule);

    Request request = new Request.Builder().url(this.webSocketUrl).build();
    this.reactiveWebSocket = new ReactiveWebSocketImpl(okHttpClient, request);
    this.socketConnection = reactiveWebSocket.connect()
        .retryWhen(throwableFlow -> throwableFlow.delay(3, TimeUnit.SECONDS))
        .replay(1)
        .autoConnect();

    // @NonNull Disposable outgoingMessagesDisposible =
    // TODO Wrap around Closeable
    socketConnection.ofType(SendCapable.class)
        .switchMap(state -> outgoingMessageProcessor.doOnNext(state::send))
        .subscribe();
  }

  @Override
  public void close() throws IOException {
    reactiveWebSocket.close();
  }
}
