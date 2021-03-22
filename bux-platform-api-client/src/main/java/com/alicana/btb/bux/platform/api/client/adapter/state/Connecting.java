package com.alicana.btb.bux.platform.api.client.adapter.state;

import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Returned immediately after subscribing.
 * </p>
 * Connecting state enables client to send message to the WebSocket while connecting.
 * In WebSocket, it is possible to queue messages before initiate connection.
 * Corresponds to the state before onOpen in WebSocketListener.
 */
public final record Connecting(WebSocket webSocket) implements SocketState, SendCapable {

  @Override
  public void send(String text) {
    webSocket.send(text);
  }

  @Override
  public void send(ByteString text) {
    webSocket.send(text);
  }
}
