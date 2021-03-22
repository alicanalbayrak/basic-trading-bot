package com.alicana.btb.bux.platform.api.client.adapter.impl;

import com.alicana.btb.bux.platform.api.client.adapter.ReactiveWebSocket;
import com.alicana.btb.bux.platform.api.client.adapter.ReactiveWebSocketListener;
import com.alicana.btb.bux.platform.api.client.adapter.state.SocketState;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import java.io.IOException;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link ReactiveWebSocket}.
 */
public class ReactiveWebSocketImpl implements ReactiveWebSocket {

  private static final Logger log = LoggerFactory.getLogger(ReactiveWebSocketImpl.class);

  private final WebSocket.Factory factory;
  private final Request wsRequest;
  private WebSocket webSocket = null;

  public ReactiveWebSocketImpl(WebSocket.Factory factory, Request wsRequest) {
    this.factory = factory;
    this.wsRequest = wsRequest;
  }

  @Override
  public Flowable<SocketState> connect() {

    return Flowable.create(this::openSocketAndListen, BackpressureStrategy.LATEST)
        .doFinally(() -> {
          // TODO LOG signal
          if (webSocket != null) {
            // TODO hard-coded!
            webSocket.close(1000, "close");
            webSocket = null;
          }
        });
  }

  @Override
  public void close() throws IOException {
    // TODO implement
  }

  private void openSocketAndListen(final FlowableEmitter<SocketState> emitter) {
    ReactiveWebSocketListener listener = new ReactiveWebSocketListenerImpl(emitter);
    webSocket = factory.newWebSocket(wsRequest, listener);
    listener.onCreate(webSocket);
  }
}
