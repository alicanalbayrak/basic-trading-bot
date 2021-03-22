package com.alicana.btb.bux.platform.api.client.config;

import static com.alicana.btb.bux.platform.api.client.example.ExampleUtil.getToken;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

class BuxApiConfigTest {

  @Test
  void shouldHaveValues() {

    // GIVEN
    final String wsUrl = "wss://rtf.beta.getbux.com/subscriptions/me";
    final String restUrl = "https://api.beta.getbux.com/core/21/";
    final String authToken = getToken();

    // WHEN
    BuxApiConfig buxApiConfig = new BuxApiConfig(wsUrl, restUrl, authToken);

    // THEN
    assertThat(buxApiConfig.webSocketUrl()).isEqualTo(wsUrl);
    assertThat(buxApiConfig.restUrl()).isEqualTo(restUrl);
    assertThat(buxApiConfig.authToken()).isEqualTo(authToken);
  }
}