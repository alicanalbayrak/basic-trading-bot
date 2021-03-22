package com.alicana.btb.bux.platform.api.client.serde;

import static org.assertj.core.api.Assertions.assertThat;

import com.alicana.btb.bux.platform.api.client.model.BuxDomainEvent;
import com.alicana.btb.bux.platform.api.client.model.BuxSubscription;
import com.alicana.btb.bux.platform.api.client.model.ConnectedEvent;
import com.alicana.btb.bux.platform.api.client.model.QuoteEvent;
import com.alicana.btb.bux.platform.api.client.model.UnknownEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BuxDomainEventDeserializerTest {

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    SimpleModule module = new SimpleModule();
    module.addDeserializer(BuxDomainEvent.class, new BuxDomainEventDeserializer());


    objectMapper = new ObjectMapper();
    objectMapper.registerModule(module);
  }

  @Test
  void shouldDeserializeConnectedEvent() throws JsonProcessingException {

    String json = """
        {
          "t": "connect.connected",
          "body": {
              "userId": "4726",
              "sessionId": "1234-233"
          }
        }
        """;

    BuxDomainEvent result = objectMapper.readValue(json, BuxDomainEvent.class);

    assertThat(result).isExactlyInstanceOf(ConnectedEvent.class);
    ConnectedEvent connectedEvent = (ConnectedEvent) result;
    assertThat(connectedEvent.userId()).isEqualTo("4726");
    assertThat(connectedEvent.sessionId()).isEqualTo("1234-233");
  }

  @Test
  void shouldDeserializeQuoteEvent() throws JsonProcessingException {

    String json = """
        {
          "t":"trading.quote",
          "id":"3955bd64-88c4-11eb-a615-fd991a23c1f3",
          "v":2,
          "body":{
            "securityId":"sb26493",
            "currentPrice":"14638.5"
            }
          }
        """;

    BuxDomainEvent result = objectMapper.readValue(json, BuxDomainEvent.class);

    assertThat(result).isExactlyInstanceOf(QuoteEvent.class);
    QuoteEvent quoteEvent = (QuoteEvent) result;
    assertThat(quoteEvent.securityId()).isEqualTo("sb26493");
    assertThat(quoteEvent.currentPrice()).isEqualTo("14638.5");
  }

  @Test
  void sbouldDeserializePortfolioPerformance() throws JsonProcessingException {

    String json = """
        {
          "t":"portfolio.performance",
          "id":"6d545fe5-8a1e-11eb-a955-9b90c917da6f",
          "v":1,
          "body":{
              "accountValue": {
                "currency":"BUX",
                "decimals":5,
                "amount":"18689.66000"
              },
              "performance":"0.1447",
              "suggestFunding":false
          }
        }
        """;

    BuxDomainEvent result = objectMapper.readValue(json, BuxDomainEvent.class);

    assertThat(result).isExactlyInstanceOf(UnknownEvent.class);
    System.out.println(result);
  }

  @Test
  void shouldDeserializeBuxSubscriptionEvent() throws JsonProcessingException {

    String json = """
        {
          "subscribeTo":["TRADING.PRODUCT.SB26493"],
          "unsubscribeFrom":[]
        }
        """;

    BuxDomainEvent result = objectMapper.readValue(json, BuxDomainEvent.class);
    assertThat(result).isExactlyInstanceOf(BuxSubscription.class);
    BuxSubscription buxSubscription = (BuxSubscription) result;
    assertThat(buxSubscription.subscribeTo()).hasSize(1);
    assertThat(buxSubscription.subscribeTo()).contains("TRADING.PRODUCT.SB26493");
    assertThat(buxSubscription.unsubscribeFrom()).hasSize(0);
  }
}