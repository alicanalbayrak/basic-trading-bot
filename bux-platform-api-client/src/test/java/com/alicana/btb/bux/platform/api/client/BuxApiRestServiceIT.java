package com.alicana.btb.bux.platform.api.client;

import static com.alicana.btb.bux.platform.api.client.util.AuthorizationTestTokenProvider.getToken;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.alicana.btb.bux.platform.api.client.config.BuxApiConfig;
import com.alicana.btb.bux.platform.api.client.model.BigMoney;
import com.alicana.btb.bux.platform.api.client.model.OpenPosition;
import com.alicana.btb.bux.platform.api.client.model.Source;
import com.alicana.btb.bux.platform.api.client.model.Trade;
import com.alicana.btb.bux.platform.api.client.model.TradeDirection;
import com.alicana.btb.bux.platform.api.client.util.MockDispatcher;
import java.io.IOException;
import java.math.BigDecimal;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BuxApiRestServiceIT {

  private static final Logger log = LoggerFactory.getLogger(BuxApiRestServiceIT.class);

  private MockWebServer server;
  private BuxApiRestClient buxRestClient;

  @BeforeEach
  void setUp() throws IOException {

    this.server = new MockWebServer();
    this.server.start();

    final String restUrl = server.url("core/21/").toString();
    final String wsUrl = "ws://localhost:8080/subscriptions/me";
    final String authToken = getToken();

    var config = new BuxApiConfig(wsUrl, restUrl, authToken);
    this.buxRestClient = BuxApiClientFactory.newInstance(config).newRestClient();
  }

  @Test
  void shouldSuccessfullyExecuteOpenPosition() {

    // GIVEN
    this.server.setDispatcher(new MockDispatcher());

    // WHEN
    BigMoney bm = new BigMoney("BUX", 2, new BigDecimal(12));
    Source source = new Source("sourceType", "sourceId");
    OpenPosition openPos = new OpenPosition("sb26493", bm, 1, TradeDirection.BUY, source);

    Trade openReqResult = buxRestClient.openPosition(openPos);
    log.info(openReqResult.toString());
    assertThat(openReqResult.direction()).isEqualTo(TradeDirection.BUY);

    Trade closeRequestResult = buxRestClient.closePosition(openReqResult.positionId());
    log.info(closeRequestResult.toString());
  }

}