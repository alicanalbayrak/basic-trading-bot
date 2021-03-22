package com.alicana.btb.bux.platform.api.client.example;


import static com.alicana.btb.bux.platform.api.client.example.TestUtil.getToken;

import com.alicana.btb.bux.platform.api.client.BuxApiClientFactory;
import com.alicana.btb.bux.platform.api.client.BuxApiRestClient;
import com.alicana.btb.bux.platform.api.client.BuxApiWebSocketClient;
import com.alicana.btb.bux.platform.api.client.config.BuxApiConfig;
import com.alicana.btb.bux.platform.api.client.model.BigMoney;
import com.alicana.btb.bux.platform.api.client.model.BuxSubscription;
import com.alicana.btb.bux.platform.api.client.model.ConnectedEvent;
import com.alicana.btb.bux.platform.api.client.model.OpenPosition;
import com.alicana.btb.bux.platform.api.client.model.QuoteEvent;
import com.alicana.btb.bux.platform.api.client.model.Source;
import com.alicana.btb.bux.platform.api.client.model.Trade;
import com.alicana.btb.bux.platform.api.client.model.TradeDirection;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamingQuoteEventsExample {

  private static final Logger log = LoggerFactory.getLogger(StreamingQuoteEventsExample.class);

  public static void main(String[] args) {

    log.info("started");

    ObjectMapper objectMapper = new ObjectMapper();

    final String wsUrl = "wss://rtf.beta.getbux.com/subscriptions/me";
//    final String wsUrl = "ws://localhost:8080/subscriptions/me";
    final String restUrl = "https://api.beta.getbux.com/core/21/";
    final String authToken = getToken();

    var config = new BuxApiConfig(wsUrl, restUrl, authToken);
    BuxApiClientFactory clientFactory = BuxApiClientFactory.newInstance(config);
    BuxApiWebSocketClient buxApiWebSocketClient = clientFactory.newWebSocketClient();
    BuxApiRestClient buxRestClient = clientFactory.newRestClient();

    AtomicBoolean subscribedToProduct = new AtomicBoolean(false);
    AtomicBoolean positionOpened = new AtomicBoolean(false);

    buxApiWebSocketClient.observeMessages()
        .ofType(ConnectedEvent.class)
        .doOnNext(connectedEvent -> {
          log.info("Received connected event");

          List<String> a = Collections.singletonList("trading.product.sb26493");
          BuxSubscription subs = new BuxSubscription(a, Collections.emptyList());

          buxApiWebSocketClient.sendMessage(objectMapper.writeValueAsString(subs));
          subscribedToProduct.getAndSet(true);
        })
        .subscribe();

    buxApiWebSocketClient.observeMessages()
        .ofType(BuxSubscription.class)
        .doOnNext(buxSubscription -> {
          log.info(buxSubscription.toString());
          subscribedToProduct.getAndSet(true);

        })
        .subscribe();

    @NonNull Disposable a =
        buxApiWebSocketClient.observeMessages()
            .ofType(QuoteEvent.class)
            .doOnNext(quoteEvent -> {

              log.info(quoteEvent.toString());
              String currentPrice = quoteEvent.currentPrice();

              if (subscribedToProduct.get() && !positionOpened.get()) {
                BigMoney bm = new BigMoney("BUX", 2, new BigDecimal("8000"));
                Source source = new Source("OTHER", null);
                OpenPosition openPos =
                    new OpenPosition("sb26493", bm, 1, TradeDirection.BUY, source);

                Trade trade =
                    buxRestClient.openPosition(openPos);

                log.info(trade.toString());


                Trade closedResult = buxRestClient.closePosition(trade.positionId());
                log.info(closedResult.toString());
              }

            })
            .subscribe();

    while (!a.isDisposed()) {
      sleep();
    }
  }

  private static void sleep() {
    try {
      log.info("Sleeping " + Thread.currentThread().getName());
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
