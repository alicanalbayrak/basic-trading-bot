package com.alicana.btb.bux.platform.api.client.example;

import static com.alicana.btb.bux.platform.api.client.util.AuthorizationTestTokenProvider.getToken;

import com.alicana.btb.bux.platform.api.client.BuxApiClientFactory;
import com.alicana.btb.bux.platform.api.client.BuxApiRestClient;
import com.alicana.btb.bux.platform.api.client.config.BuxApiConfig;
import com.alicana.btb.bux.platform.api.client.model.BigMoney;
import com.alicana.btb.bux.platform.api.client.model.OpenPosition;
import com.alicana.btb.bux.platform.api.client.model.Source;
import com.alicana.btb.bux.platform.api.client.model.Trade;
import com.alicana.btb.bux.platform.api.client.model.TradeDirection;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestClientExample {

  private static final Logger log = LoggerFactory.getLogger(RestClientExample.class);

  public static void main(String[] args) {

    final String wsUrl = "ws://localhost:8080/subscriptions/me";
    final String restUrl = "https://api.beta.getbux.com/core/21/";
//    final String restUrl = "http://localhost:8080/core/21/";
    final String authToken = getToken();

    var config = new BuxApiConfig(wsUrl, restUrl, authToken);
    BuxApiRestClient buxRestClient =
        BuxApiClientFactory.newInstance(config).newRestClient();

    BigMoney bm = new BigMoney("BUX", 2, new BigDecimal(12));
    Source source = new Source("sourceType", "sourceId");

    OpenPosition openPos = new OpenPosition("sb26493", bm, 1, TradeDirection.BUY, source);

    Trade openReqResult = buxRestClient.openPosition(openPos);
    log.info(openPos.toString());

    Trade closeRequestResult = buxRestClient.closePosition(openReqResult.positionId());
    log.info(closeRequestResult.toString());
  }

}
