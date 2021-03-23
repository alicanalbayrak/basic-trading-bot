package com.alicana.btb.bux.platform.api.client;


import com.alicana.btb.bux.platform.api.client.config.BuxApiConfig;
import com.alicana.btb.bux.platform.api.client.interceptor.AuthenticationInterceptor;
import java.util.concurrent.TimeUnit;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory for creating BuxApi client objects.
 */
public class BuxApiClientFactory {

  /**
   * The Bux API connection configurations.
   */
  private final BuxApiConfig buxApiConfig;

  /**
   * HTTP client.
   */
  private final OkHttpClient okHttpClient;

  /**
   * Private c'tor to initiate factory instance.
   *
   * @param buxApiConfig Bux API connection configurations
   */
  private BuxApiClientFactory(final BuxApiConfig buxApiConfig) {
    this.buxApiConfig = buxApiConfig;
    this.okHttpClient = createHttpClient(buxApiConfig.authToken());
  }

  /**
   * New instance.
   *
   * @param buxApiConfig The Bux API config
   * @return The BuxApiClientFactory instance
   */
  public static BuxApiClientFactory newInstance(final BuxApiConfig buxApiConfig) {
    return new BuxApiClientFactory(buxApiConfig);
  }

  /**
   * Creates a new REST client.
   */
  public BuxApiRestClient newRestClient() {
    return new BuxApiRestClientImpl(this.okHttpClient, this.buxApiConfig);
  }

  /**
   * Creates a new web socket client used for handling data streams and sending message.
   */
  public BuxApiWebSocketClient newWebSocketClient() {
    return new BuxApiWebSocketClientImpl(this.okHttpClient, this.buxApiConfig.webSocketUrl());
  }

  /**
   * Creates OKHttp client with configuration.
   *
   * @param authToken Authorization header token
   * @return OkHttp client
   */
  private OkHttpClient createHttpClient(final String authToken) {

    final Dispatcher dispatcher = new Dispatcher();
    dispatcher.setMaxRequestsPerHost(500);
    dispatcher.setMaxRequests(500);

    final Logger logger = LoggerFactory.getLogger(OkHttpClient.class);

    final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger::info);
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    return new OkHttpClient.Builder()
        .dispatcher(dispatcher)
        .pingInterval(20, TimeUnit.SECONDS)
        .addInterceptor(new AuthenticationInterceptor(authToken))
        .addInterceptor(loggingInterceptor)
        .build();
  }

}
