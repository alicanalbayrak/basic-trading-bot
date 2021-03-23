package com.alicana.btb.bux.platform.api.client.adapter.impl;

import com.alicana.btb.bux.platform.api.client.adapter.ReactiveWebSocketListener;
import com.alicana.btb.bux.platform.api.client.adapter.state.Connected;
import com.alicana.btb.bux.platform.api.client.adapter.state.Connecting;
import com.alicana.btb.bux.platform.api.client.adapter.state.Disconnected;
import com.alicana.btb.bux.platform.api.client.adapter.state.Disconnecting;
import com.alicana.btb.bux.platform.api.client.adapter.state.SocketState;
import com.alicana.btb.bux.platform.api.client.exception.SocketStateException;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.processors.PublishProcessor;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reactive wrapper implementation for {@link ReactiveWebSocketListener}.
 * This class is responsible to orchestrate internal socket state transition
 * and deliver webSocket messages accordingly.
 */
public class ReactiveWebSocketListenerImpl extends ReactiveWebSocketListener {

  private static final Logger log = LoggerFactory.getLogger(ReactiveWebSocketListenerImpl.class);

  private final FlowableEmitter<SocketState> socketStateEmitter;
  private final PublishProcessor<String> textMsgPublisher;
  private final PublishProcessor<ByteString> byteMsgPublisher;

  /**
   * Initializes class.
   *
   * @param socketStateEmitter The socket state emitter
   */
  public ReactiveWebSocketListenerImpl(
      FlowableEmitter<SocketState> socketStateEmitter) {

    this.socketStateEmitter = socketStateEmitter;
    this.textMsgPublisher = PublishProcessor.create();
    this.byteMsgPublisher = PublishProcessor.create();
  }

  @Override
  public void onCreate(WebSocket webSocket) {
    log.info("Reactive web socket listener created.");
    socketStateEmitter.onNext(new Connecting(webSocket));
  }

  @Override
  public void onOpen(WebSocket webSocket, Response response) {
    log.info("WebSocket is opened");
    super.onOpen(webSocket, response);
    socketStateEmitter.onNext(new Connected(webSocket, textMsgPublisher));
  }

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    super.onMessage(webSocket, text);
    textMsgPublisher.onNext(text);
  }

  @Override
  public void onMessage(WebSocket webSocket, ByteString bytes) {
    super.onMessage(webSocket, bytes);
    byteMsgPublisher.onNext(bytes);
  }

  @Override
  public void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
    log.error("WebSocket failure!", throwable);
    super.onFailure(webSocket, throwable, response);
    socketStateEmitter.tryOnError(new SocketStateException(throwable, response));
  }

  @Override
  public void onClosing(WebSocket webSocket, int code, String reason) {
    log.info("Closing webSocket...");
    super.onClosing(webSocket, code, reason);
    socketStateEmitter.onNext(new Disconnecting());
  }

  @Override
  public void onClosed(WebSocket webSocket, int code, String reason) {
    log.info("WebSocket closed...");
    super.onClosed(webSocket, code, reason);
    socketStateEmitter.onNext(new Disconnected());
    socketStateEmitter.onComplete();
  }


}
