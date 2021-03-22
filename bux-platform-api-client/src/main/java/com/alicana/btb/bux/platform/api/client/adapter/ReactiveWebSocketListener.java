package com.alicana.btb.bux.platform.api.client.adapter;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Extends {@link WebSocketListener} with reactive support.
 */
public abstract class ReactiveWebSocketListener extends WebSocketListener {

  /**
   * Pass {@link WebSocket} connection to the {@link ReactiveWebSocketListener} implementation
   * to be able to issue web socket requests rom client.
   */
  public abstract void onCreate(final WebSocket webSocket);

}
