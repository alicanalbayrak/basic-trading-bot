package com.alicana.btb.bux.platform.trading.app;

import com.alicana.btb.bux.platform.api.client.BuxApiClientFactory;
import com.alicana.btb.bux.platform.api.client.BuxApiRestClient;
import com.alicana.btb.bux.platform.api.client.BuxApiWebSocketClient;
import com.alicana.btb.bux.platform.api.client.config.BuxApiConfig;
import com.alicana.btb.bux.platform.trading.app.config.BuxApiClientConfigurationProvider;
import com.alicana.btb.bux.platform.trading.app.exception.TradingBotException;
import com.alicana.btb.bux.platform.trading.app.model.TradingBotInputParams;
import com.alicana.btb.bux.platform.trading.app.service.MarketDataStreamingService;
import com.alicana.btb.bux.platform.trading.app.service.TradeService;
import com.alicana.btb.bux.platform.trading.app.service.TradeStrategy;
import com.alicana.btb.bux.platform.trading.app.service.impl.BuxMarketDataStreamingServiceImpl;
import com.alicana.btb.bux.platform.trading.app.service.impl.BuxTradeServiceImpl;
import com.alicana.btb.bux.platform.trading.app.service.impl.SimpleTradeStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry point to the trading bot.
 */
public record BasicTradingBot(TradingBotInputParams tradingBotInputParams) {

  private static final Logger log = LoggerFactory.getLogger(BasicTradingBot.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Factory for {@link BasicTradingBot}.
   *
   * @return New instance
   */
  public static BasicTradingBot create(final String userJsonInput) {

    try {
      TradingBotInputParams input =
          objectMapper.readValue(userJsonInput, TradingBotInputParams.class);
      log.info("Input={}", input);
      return new BasicTradingBot(input);
    } catch (JsonProcessingException e) {
      final String errMsg =
          String.format("Could not parse json user input! Input=%s", userJsonInput);
      log.error(errMsg);
      throw new TradingBotException(errMsg);
    }
  }

  /**
   * Start mechanism to consume market data events and execute strategy accordingly.
   */
  public void run() throws ExecutionException, InterruptedException {

    BuxApiConfig buxApiConfig =
        BuxApiClientConfigurationProvider.readConfigurations();

    BuxApiWebSocketClient webSocketClient =
        BuxApiClientFactory.newInstance(buxApiConfig).newWebSocketClient();

    BuxApiRestClient restClient =
        BuxApiClientFactory.newInstance(buxApiConfig).newRestClient();

    TradeService tradeService = new BuxTradeServiceImpl(restClient);
    TradeStrategy tradeStrategy = new SimpleTradeStrategy(tradingBotInputParams, tradeService);
    MarketDataStreamingService marketDataStreamingService =
        new BuxMarketDataStreamingServiceImpl(webSocketClient, tradeStrategy);

    CompletableFuture<Void> future =
        marketDataStreamingService.startMarketDataStream(tradingBotInputParams().productId());
    future.get();
  }


}
