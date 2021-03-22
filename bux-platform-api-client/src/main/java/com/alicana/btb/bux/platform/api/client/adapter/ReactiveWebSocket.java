package com.alicana.btb.bux.platform.api.client.adapter;

import com.alicana.btb.bux.platform.api.client.adapter.state.SocketState;
import io.reactivex.rxjava3.core.Flowable;
import java.io.Closeable;

/**
 * Adapter interface for WebSocket client.
 */
public interface ReactiveWebSocket extends Closeable {

  /*
   * Creates WebSocket connection and starts publishing events.
   *
   * @return Reactive messageFlowable of {@link SocketStateBaseEvent}
   */
  Flowable<SocketState> connect();

}
