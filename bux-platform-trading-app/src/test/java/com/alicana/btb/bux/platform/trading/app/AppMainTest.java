package com.alicana.btb.bux.platform.trading.app;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class AppMainTest {

  @Test
  void shouldFailWhenArgsArrayIsNull() {

    assertThatThrownBy(() -> AppMain.main(null))
        .hasMessageContaining("Missing trading bot arguments!");
  }

  @Test
  void shouldFailWhenArgIsEmpty() {

    assertThatThrownBy(() -> AppMain.main(new String[1]))
        .hasMessageContaining("Missing trading bot arguments!");
  }

  @Test
  void shouldFailWhenArgsIsEmptyString() {

    assertThatThrownBy(() -> AppMain.main(new String[] {""}))
        .hasMessageContaining("Missing trading bot arguments!");
  }

  @Test
  void shouldFailWhenArgsIsWhitespace() {

    assertThatThrownBy(() -> AppMain.main(new String[] {" "}))
        .hasMessageContaining("Missing trading bot arguments!");
  }

  @Test
  void shouldFailWhenArgsInvalid() {

    assertThatThrownBy(() -> AppMain.main(new String[] {"{"}))
        .hasMessageContaining("Could not parse json user input!");
  }

  @Disabled
  @Test
  void shouldExecuteSuccessfully() {

    final String inputJson = """
        { "productId":"sb26502", "buyPrice": "1.18231", "upperSellLimit": "1.18242", "lowerSellLimit": "1.18230" }
        """;

    AppMain.main(new String[] {inputJson});
  }

}