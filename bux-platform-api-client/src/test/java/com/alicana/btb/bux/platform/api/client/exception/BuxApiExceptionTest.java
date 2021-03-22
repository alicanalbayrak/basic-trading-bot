package com.alicana.btb.bux.platform.api.client.exception;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.alicana.btb.bux.platform.api.client.model.BuxRestError;
import org.junit.jupiter.api.Test;

class BuxApiExceptionTest {

  @Test
  void shouldReturnBuxApiError_whenNotNull() {

    // GIVEN
    final String message = "err msg";
    final String developerMessage = "dev msg";
    final String errCode = "code";
    final String exceptionMsg = String.format("Msg: %s, DevMsg: %s", message, developerMessage);
    BuxRestError buxRestError = new BuxRestError(message, developerMessage, errCode);
    // WHEN
    BuxApiException exception = new BuxApiException(buxRestError);

    // THEN
    assertThat(exception.getApiError()).isNotEmpty();
    BuxRestError errorInstanceFromException = exception.getApiError().get();
    assertThat(errorInstanceFromException).isEqualTo(buxRestError);
    assertThat(errorInstanceFromException.message()).isEqualTo(message);
    assertThat(errorInstanceFromException.developerMessage()).isEqualTo(developerMessage);
    assertThat(errorInstanceFromException.errorCode()).isEqualTo(errCode);

    assertThat(exception.getMessage()).isEqualTo(exceptionMsg);
  }

  @Test
  void shouldReturnBuxApiError_whenAbsent() {

    // GIVEN
    final String throwableMsg = "Something went wrong";

    // WHEN
    BuxApiException exception = new BuxApiException(new Throwable(throwableMsg));

    // THEN
    assertThat(exception.getApiError()).isEmpty();
    assertThat(exception.getMessage()).isEqualTo("java.lang.Throwable: " + throwableMsg);
  }

}