package com.alicana.btb.bux.platform.api.client.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ModelSerdeTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void shouldResolveTradePayloadFromJSON() throws JsonProcessingException {

    String jsonString = """
        {
          "id": "9bc887eb-23d2-4293-a139-1da9c4eb5f1c",
          "positionId": "655ddda5-fd6d-48a9-800d-b7b93eb041af",
          "product": {
            "securityId": "26618",
            "symbol": "EUR/USD",
            "displayName": "EUR/USD"
          },
          "investingAmount": {
            "currency": "BUX",
            "decimals": 2,
            "amount": "10.00"
        }, "price": {
            "currency": "USD",
            "decimals": 5,
            "amount": "1.07250"
          },
          "leverage": 2,
          "direction": "BUY",
          "type": "OPEN",
          "dateCreated": 1492601296549
        }
        """;

    Trade trade = objectMapper.readValue(jsonString, Trade.class);

    assertThat(trade.id()).isEqualTo("9bc887eb-23d2-4293-a139-1da9c4eb5f1c");
    assertThat(trade.positionId()).isEqualTo("655ddda5-fd6d-48a9-800d-b7b93eb041af");
    assertThat(trade.leverage()).isEqualTo(2);
    assertThat(trade.direction()).isEqualTo(TradeDirection.BUY);
    assertThat(trade.type()).isEqualTo(TradeType.OPEN);
    // product
    assertThat(trade.product().securityId()).isEqualTo("26618");
    assertThat(trade.product().symbol()).isEqualTo("EUR/USD");
    assertThat(trade.product().displayName()).isEqualTo("EUR/USD");
    // investing amount
    assertThat(trade.investingAmount().currency()).isEqualTo("BUX");
    assertThat(trade.investingAmount().decimalPlaces()).isEqualTo(2);
    assertThat(trade.investingAmount().amount()).isEqualTo("10.00");
    // price
    assertThat(trade.price().currency()).isEqualTo("USD");
    assertThat(trade.price().decimalPlaces()).isEqualTo(5);
    assertThat(trade.price().amount()).isEqualTo("1.07250");
  }


  @Test
  void shouldSuccessfullySerializeOpenPosition() throws JsonProcessingException {

    final String productId = "26618";
    final int leverage = 2;
    final TradeDirection tradeDirection = TradeDirection.BUY;
    final String sourceType = "OTHER";
    final String sourceId = "37e5158e-6f74-4446-85f7-47d037af4145";

    final Source source = new Source(sourceType, sourceId);
    final BigMoney investingAmount = new BigMoney("BUX", 2, new BigDecimal("10.00"));
    OpenPosition openPosition =
        new OpenPosition(productId, investingAmount, leverage, tradeDirection, source);

    String openPositionJson = objectMapper.writeValueAsString(openPosition);

    JsonNode jsonNodeActual = objectMapper.readTree(openPositionJson);
    JsonNode jsonNodeSample = objectMapper.readTree(getSampleOpenPositionJson());

    assertThat(jsonNodeActual).isEqualTo(jsonNodeSample);

  }

  private String getSampleOpenPositionJson() {
    return """
        {
          "productId":"26618",
          "investingAmount":{
            "currency":"BUX",
            "decimals":2,
            "amount":10.00
            },
          "leverage":2,
          "direction":"BUY",
          "source":{
            "sourceType":"OTHER",
            "sourceId":"37e5158e-6f74-4446-85f7-47d037af4145"
          }
        }
        """;
  }

}