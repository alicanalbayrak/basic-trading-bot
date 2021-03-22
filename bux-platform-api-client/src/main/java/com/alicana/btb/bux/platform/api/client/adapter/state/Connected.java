package com.alicana.btb.bux.platform.api.client.adapter.state;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Returned when the socket has successfully opened connection.
 * </p>
 * Connected state record, provides {@link WebSocket} instance and {@code Flowable<String>}
 * in order to the client in order to send/receive messages from WebSocket in reactive manner.
 */
public final record Connected(WebSocket webSocketConn, Flowable<String> messageFlowable)
    implements SocketState, SendCapable {

  @Override
  public void send(String text) {
    webSocketConn.send(text);
  }

  @Override
  public void send(ByteString text) {
    webSocketConn.send(text);
  }
}
