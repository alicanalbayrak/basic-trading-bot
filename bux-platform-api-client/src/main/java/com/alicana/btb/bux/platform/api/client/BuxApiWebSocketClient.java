package com.alicana.btb.bux.platform.api.client;

import com.alicana.btb.bux.platform.api.client.adapter.state.SocketState;
import com.alicana.btb.bux.platform.api.client.model.BuxDomainEvent;
import io.reactivex.rxjava3.core.Flowable;

/**
 * Bux API data streaming facade, supporting streaming of events through web sockets.
 */
public interface BuxApiWebSocketClient {

  /**
   * An {@link Flowable} instance that serve Bux WS API messages in plain text.
   *
   * @return Flowable instance
   */
  Flowable<BuxDomainEvent> observeMessages();

  /**
   * An {@link Flowable} instance that serve internal socket state.
   *
   * @return Flowable instance
   */
  Flowable<SocketState> observeSocketState();

  /**
   * Sends message on connected WebSocket session.
   *
   * @param msg message
   */
  void sendMessage(final String msg);

}
