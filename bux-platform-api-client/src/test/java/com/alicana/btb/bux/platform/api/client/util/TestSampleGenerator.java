package com.alicana.btb.bux.platform.api.client.util;

import com.alicana.btb.bux.platform.api.client.model.BigMoney;
import com.alicana.btb.bux.platform.api.client.model.Product;
import com.alicana.btb.bux.platform.api.client.model.Trade;
import com.alicana.btb.bux.platform.api.client.model.TradeDirection;
import com.alicana.btb.bux.platform.api.client.model.TradeType;
import java.math.BigDecimal;

public class TestSampleGenerator {

  private TestSampleGenerator() {
    // do nothing
  }

  public static Trade generateSampleTrade(final String uuid,
                                          final TradeDirection tradeDirection,
                                          final TradeType tradeType) {

    BigMoney mockBigMoney = new BigMoney("BUX", 2, new BigDecimal("12.2"));
    Product product = new Product("securityId", "symbol", "display");
    return new Trade("id", uuid, mockBigMoney, product, mockBigMoney, mockBigMoney, 1,
        tradeDirection, tradeType);
  }

  public static String restErrBodyString() {
    return """
        {
          "message": "may be null",
          "developerMessage": "technical description of the error.",
          "errorCode": "CORE_002"
        }
        """;
  }
}


