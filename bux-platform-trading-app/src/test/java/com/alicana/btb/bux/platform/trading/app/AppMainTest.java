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
        { "productId":"sb26493", "buyPrice": 17.203, "upperSellLimit": 20.000,"lowerSellLimit":15.000}
        """;

    AppMain.main(new String[] {inputJson});
  }

}