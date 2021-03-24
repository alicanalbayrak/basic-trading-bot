package com.alicana.btb.bux.platform.trading.app.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class TradingBotInputParamsTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void shouldThrowException_whenMissingField() {

    final String json = """
        {
          "productId":"sbXXX",
          "buyPrice": 17.203,
          "upperSellLimit": 20.000
        }
        """;

    assertThatThrownBy(() -> objectMapper.readValue(json, TradingBotInputParams.class))
        .isExactlyInstanceOf(MismatchedInputException.class);
  }

  @Test
  void shouldSuccessfullyDeserialize() throws JsonProcessingException {

    final String json = """
        {
          "productId":"sbXYZ",
          "buyPrice": 17.203,
          "upperSellLimit": 20.000,
          "lowerSellLimit":15.000
        }
        """;

    TradingBotInputParams tbInputParams = objectMapper.readValue(json, TradingBotInputParams.class);

    assertThat(tbInputParams.productId()).isEqualTo("sbXYZ");
    assertThat(tbInputParams.buyPrice()).isEqualTo(new BigDecimal("17.203"));
    assertThat(tbInputParams.upperSellLimit()).isEqualTo(new BigDecimal("20.000"));
    assertThat(tbInputParams.lowerSellLimit()).isEqualTo(new BigDecimal("15.000"));
    assertThat(tbInputParams.isValid()).isTrue();
  }

  @Test
  void testTradingBotInputParamsValidation() throws JsonProcessingException {

    TradingBotInputParams buyPriceEqualUpperLimit =
        new TradingBotInputParams("x", new BigDecimal("12"),
            new BigDecimal("12"), new BigDecimal("11.99"));

    assertThat(buyPriceEqualUpperLimit.isValid()).isFalse();

    TradingBotInputParams buyPriceEqualLowerLimit =
        new TradingBotInputParams("x", new BigDecimal("12"),
            new BigDecimal("18"), new BigDecimal("12"));

    assertThat(buyPriceEqualLowerLimit.isValid()).isFalse();

    TradingBotInputParams validInstance =
        new TradingBotInputParams("x", new BigDecimal("15"),
            new BigDecimal("18"), new BigDecimal("12"));

    assertThat(validInstance.isValid()).isTrue();
  }

}