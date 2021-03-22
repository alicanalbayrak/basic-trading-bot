package com.alicana.btb.bux.platform.api.client.adapter.state;

import okio.ByteString;

/**
 * Indicates that the implementor of this class can send a message
 * both in the form of {@code String} and {@code ByteString}.
 */
public interface SendCapable {

  /**
   * Send plain text message to WebSocket channel.
   *
   * @param text message
   */
  void send(final String text);

  /**
   * Send byte string message to WebSocket channel.
   *
   * @param text message in byte string format
   */
  void send(final ByteString text);

}
