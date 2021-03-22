package com.alicana.btb.bux.platform.api.client.exception;

import com.alicana.btb.bux.platform.api.client.model.BuxRestError;
import java.io.Serializable;
import java.util.Optional;

/**
 * An exception which can occur while invoking methods of the Bux API.
 */
public class BuxApiException extends RuntimeException implements Serializable {

  private BuxRestError apiError;

  /**
   * Create exception instance with api error.
   *
   * @param apiError The Bux Rest API error.
   */
  public BuxApiException(BuxRestError apiError) {
    this.apiError = apiError;
  }

  public BuxApiException(Throwable cause) {
    super(cause);
  }

  public Optional<BuxRestError> getApiError() {
    return Optional.ofNullable(apiError);
  }

  @Override
  public String getMessage() {

    return getApiError()
        .map(err -> String.format("Msg: %s, DevMsg: %s", err.message(), err.developerMessage()))
        .orElse(super.getMessage());
  }
}
