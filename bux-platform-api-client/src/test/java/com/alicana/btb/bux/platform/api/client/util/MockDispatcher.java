package com.alicana.btb.bux.platform.api.client.util;

import static com.alicana.btb.bux.platform.api.client.util.TestSampleGenerator.generateSampleTrade;
import static com.alicana.btb.bux.platform.api.client.util.TestSampleGenerator.restErrBodyString;

import com.alicana.btb.bux.platform.api.client.model.OpenPosition;
import com.alicana.btb.bux.platform.api.client.model.Trade;
import com.alicana.btb.bux.platform.api.client.model.TradeDirection;
import com.alicana.btb.bux.platform.api.client.model.TradeType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockDispatcher extends Dispatcher {

  private static final Logger log = LoggerFactory.getLogger(MockDispatcher.class);
  private final ObjectMapper objectMapper = new ObjectMapper();

  private Map<String, OpenPosition> positionMap = new ConcurrentHashMap<>();

  @Override
  public MockResponse dispatch(RecordedRequest recordedRequest) throws InterruptedException {

    if (recordedRequest.getPath().equals("/core/21/users/me/trades")) {
      return handleOpenPosition(recordedRequest);
    } else if (recordedRequest.getPath().matches("/core/21/users/me/portfolio/positions/.*")) {
      return handleClosePosition(recordedRequest);
    } else {
      return getMockErrorResponse();
    }
  }

  private MockResponse handleOpenPosition(final RecordedRequest recordedRequest) {
    log.info("Received open position request");

    try {
      OpenPosition openPosition =
          objectMapper.readValue(recordedRequest.getBody().readByteArray(), OpenPosition.class);

      UUID uuid = UUID.randomUUID();
      positionMap.put(uuid.toString(), openPosition);
      Trade trade = generateSampleTrade(uuid.toString(), TradeDirection.BUY, TradeType.OPEN);

      return new MockResponse().setResponseCode(200)
          .setBody(objectMapper.writeValueAsString(trade));

    } catch (IOException e) {
      log.error("Something went wrong while deserializing the the request or response body", e);
    }

    return getMockErrorResponse();
  }

  private MockResponse handleClosePosition(final RecordedRequest recordedRequest) {
    log.info("Received close  position request");

    String positionId = recordedRequest.getRequestUrl().pathSegments().get(6);

    if (this.positionMap.containsKey(positionId)) {
      this.positionMap.remove(positionId);
      Trade trade = generateSampleTrade(positionId, TradeDirection.BUY, TradeType.OPEN);
      try {
        return new MockResponse().setResponseCode(200)
            .setBody(objectMapper.writeValueAsString(trade));
      } catch (JsonProcessingException e) {
        log.error("Something went wrong while serializing the the response body", e);
      }
    }

    return getMockErrorResponse();
  }

  private MockResponse getMockErrorResponse() {
    return new MockResponse().setResponseCode(400)
        .setBody(restErrBodyString());
  }


}
