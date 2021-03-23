package com.alicana.btb.bux.platform.trading.app.config;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.alicana.btb.bux.platform.api.client.config.BuxApiConfig;
import org.junit.jupiter.api.Test;

class BuxApiClientConfigurationProviderTest {

  public static String getToken() {
    return "eyJhbGciOiJIUzI1NiJ9.eyJyZWZyZXNoYWJsZSI6ZmFsc2UsInN1YiI6ImJiMGNkYTJiLWE" +
        "xMGUtNGVkMy1hZDVhLTBmODJiNGMxNTJjNCIsImF1ZCI6ImJldGEuZ2V0YnV4LmNvbSIsInN" +
        "jcCI6WyJhcHA6bG9naW4iLCJydGY6bG9naW4iXSwiZXhwIjoxODIwODQ5Mjc5LCJpYXQiOjE" +
        "1MDU0ODkyNzksImp0aSI6ImI3MzlmYjgwLTM1NzUtNGIwMS04NzUxLTMzZDFhNGRjOGY5MiI" +
        "sImNpZCI6Ijg0NzM2MjI5MzkifQ.M5oANIi2nBtSfIfhyUMqJnex-JYg6Sm92KPYaUL9GKg";
  }

  @Test
  void shouldReadConfigurations() {

    final String authToken = getToken();

    BuxApiConfig buxApiConfig =
        BuxApiClientConfigurationProvider.readConfigurations();

    assertThat(buxApiConfig.webSocketUrl()).isEqualTo("wss://rtf.beta.getbux.com/subscriptions/me");
    assertThat(buxApiConfig.restUrl()).isEqualTo("https://api.beta.getbux.com/core/21/");
    assertThat(buxApiConfig.authToken()).isEqualTo(authToken);
  }

  @Test
  void shouldThrowExceptionWhenFileMissing() {

    assertThatThrownBy(
        () -> BuxApiClientConfigurationProvider.readConfigurations("not-existing.properties"));

  }
}