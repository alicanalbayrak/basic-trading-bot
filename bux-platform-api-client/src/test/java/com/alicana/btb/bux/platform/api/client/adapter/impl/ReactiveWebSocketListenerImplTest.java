package com.alicana.btb.bux.platform.api.client.adapter.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

import com.alicana.btb.bux.platform.api.client.adapter.ReactiveWebSocketListener;
import com.alicana.btb.bux.platform.api.client.adapter.state.Connected;
import com.alicana.btb.bux.platform.api.client.adapter.state.Connecting;
import com.alicana.btb.bux.platform.api.client.adapter.state.Disconnected;
import com.alicana.btb.bux.platform.api.client.adapter.state.Disconnecting;
import com.alicana.btb.bux.platform.api.client.adapter.state.SocketState;
import com.alicana.btb.bux.platform.api.client.exception.SocketStateException;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.subscribers.TestSubscriber;
import java.nio.charset.StandardCharsets;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReactiveWebSocketListenerImplTest {

  private ReactiveWebSocketListener reactiveWebSocketListener;
  private Flowable<SocketState> socketStateFlowable;
  private WebSocket webSocket;

  @BeforeEach
  void setUp() {
    this.socketStateFlowable = Flowable.create(this::bindEmitter, BackpressureStrategy.LATEST);
    this.webSocket = mock(WebSocket.class);
  }

  @Test
  void shouldPublishConnecting_onCreate() {

    TestSubscriber<SocketState> subscriber = new TestSubscriber<>();
    socketStateFlowable.subscribe(subscriber);
    reactiveWebSocketListener.onCreate(webSocket);

    subscriber.assertValuesOnly(new Connecting(webSocket));
    subscriber.assertNotComplete();
  }

  @Test
  void shouldPublishConnected_onOpen() {


    Response response = mock(Response.class);

    TestSubscriber<SocketState> subscriber = new TestSubscriber<>();
    socketStateFlowable.subscribe(subscriber);
    reactiveWebSocketListener.onOpen(webSocket, response);

    assertThat(subscriber.values().get(0)).isExactlyInstanceOf(Connected.class);
    subscriber.assertValueCount(1);
    subscriber.assertNotComplete();
  }

  @Test
  void shouldPublishDisconnecting_onClosing() {

    TestSubscriber<SocketState> subscriber = new TestSubscriber<>();
    socketStateFlowable.subscribe(subscriber);
    reactiveWebSocketListener.onClosing(webSocket, 1000, "Reason");

    subscriber.assertValuesOnly(new Disconnecting());
    subscriber.assertNotComplete();
  }

  @Test
  void shouldPublishDisconnected_onClosed() {

    TestSubscriber<SocketState> subscriber = new TestSubscriber<>();
    socketStateFlowable.subscribe(subscriber);
    reactiveWebSocketListener.onClosed(webSocket, 1000, "Reason");

    subscriber.assertValue(new Disconnected());
    subscriber.assertComplete();
  }

  @Test
  void shouldPublishFailure_onFailure() {

    RuntimeException throwable = new RuntimeException();
    Response response = mock(Response.class);

    TestSubscriber<SocketState> subscriber = new TestSubscriber<>();
    socketStateFlowable.subscribe(subscriber);
    reactiveWebSocketListener.onFailure(webSocket, throwable, response);

    subscriber.assertError(SocketStateException.class);
    subscriber.assertNotComplete();
  }

  @Test
  void shouldNotPublishAnyEvent_onPlainTextMessage() {

    TestSubscriber<SocketState> subscriber = new TestSubscriber<>();
    socketStateFlowable.subscribe(subscriber);
    reactiveWebSocketListener.onMessage(webSocket, "msg");

    subscriber.assertNoValues();
    subscriber.assertNotComplete();
  }

  @Test
  void shouldNotPublishAnyEvent_onByteStringMessage() {

    TestSubscriber<SocketState> subscriber = new TestSubscriber<>();
    socketStateFlowable.subscribe(subscriber);
    reactiveWebSocketListener
        .onMessage(webSocket, ByteString.of("msg".getBytes(StandardCharsets.UTF_8)));

    subscriber.assertNoValues();
    subscriber.assertNotComplete();
  }

  private void bindEmitter(FlowableEmitter<SocketState> emitter) {
    this.reactiveWebSocketListener = new ReactiveWebSocketListenerImpl(emitter);
  }

}