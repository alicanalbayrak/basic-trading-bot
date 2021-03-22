package com.alicana.btb.bux.platform.api.client.adapter.state;

/**
 * Published when {@code onClosing} event is received via WebSocket channel.
 */
public final record Disconnecting() implements SocketState {
}
