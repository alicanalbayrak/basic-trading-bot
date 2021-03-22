package com.alicana.btb.bux.platform.api.client.exception;

import java.io.Serializable;
import okhttp3.Response;

/**
 * Thrown when an error occurred during WebSocket connection.
 */
public class SocketStateException extends RuntimeException implements Serializable {

  private final Response response;

  public SocketStateException(Throwable cause, Response response) {
    super(cause);
    this.response = response;
  }

  public Response getResponse() {
    return response;
  }
}
