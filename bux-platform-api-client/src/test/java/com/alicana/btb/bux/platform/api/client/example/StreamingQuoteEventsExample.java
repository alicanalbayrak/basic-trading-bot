package com.alicana.btb.bux.platform.api.client.example;


import com.alicana.btb.bux.platform.api.client.BuxApiClientFactory;
import com.alicana.btb.bux.platform.api.client.BuxApiWebSocketClient;
import com.alicana.btb.bux.platform.api.client.config.BuxApiConfig;
import com.alicana.btb.bux.platform.api.client.model.BuxSubscription;
import com.alicana.btb.bux.platform.api.client.model.QuoteEvent;
import com.alicana.btb.bux.platform.api.client.model.ConnectedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamingQuoteEventsExample {

  private static final Logger log = LoggerFactory.getLogger(StreamingQuoteEventsExample.class);

  public static void main(String[] args) {

    log.info("started");

    ObjectMapper objectMapper = new ObjectMapper();

//    final String wsUrl = "wss://rtf.beta.getbux.com/subscriptions/me";
    final String wsUrl = "ws://localhost:8080/subscriptions/me";
    final String restUrl = "https://api.beta.getbux.com/core/21/users/me/trades";
    final String authToken = getToken();

    var config = new BuxApiConfig(wsUrl, restUrl, authToken);
    BuxApiWebSocketClient buxApiWebSocketClient =
        BuxApiClientFactory.newInstance(config).newWebSocketClient();

    buxApiWebSocketClient.observeMessages()
        .ofType(ConnectedEvent.class)
        .doOnNext(e -> {
          System.out.println("Received connected event");

          List<String> a = Arrays.asList("trading.product.sb26493");
          BuxSubscription subs = new BuxSubscription(a, Collections.emptyList());

          buxApiWebSocketClient.sendMessage(objectMapper.writeValueAsString(subs));
        })
        .subscribe();


    @NonNull Disposable a =
        buxApiWebSocketClient.observeMessages()
            .ofType(QuoteEvent.class)
            .doOnNext(e -> {
              System.out.println("Trading quote= " + e.toString());
            })
            .subscribe();

    while (!a.isDisposed()) {
      sleep();
    }
  }

  private static void sleep() {
    try {
      System.out.println("Sleeping " + Thread.currentThread().getName());
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static String getToken() {
    return "eyJhbGciOiJIUzI1NiJ9.eyJyZWZyZXNoYWJsZSI6ZmFsc2UsInN1YiI6ImJiMGNkYTJiLWE" +
        "xMGUtNGVkMy1hZDVhLTBmODJiNGMxNTJjNCIsImF1ZCI6ImJldGEuZ2V0YnV4LmNvbSIsInN" +
        "jcCI6WyJhcHA6bG9naW4iLCJydGY6bG9naW4iXSwiZXhwIjoxODIwODQ5Mjc5LCJpYXQiOjE" +
        "1MDU0ODkyNzksImp0aSI6ImI3MzlmYjgwLTM1NzUtNGIwMS04NzUxLTMzZDFhNGRjOGY5MiI" +
        "sImNpZCI6Ijg0NzM2MjI5MzkifQ.M5oANIi2nBtSfIfhyUMqJnex-JYg6Sm92KPYaUL9GKg";
  }
}
