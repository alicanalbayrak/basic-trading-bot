package com.alicana.btb.bux.platform.api.client.adapter.state;

/**
 * Published when {@code onClose} event is received via WebSocket channel.
 * Disconnected published just before socket state emitter is being closed.
 */
public final record Disconnected() implements SocketState {
}
