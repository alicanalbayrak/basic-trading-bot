package com.alicana.btb.bux.platform.api.client;

import static com.alicana.btb.bux.platform.api.client.BuxApiServiceGenerator.createService;
import static com.alicana.btb.bux.platform.api.client.BuxApiServiceGenerator.executeSync;

import com.alicana.btb.bux.platform.api.client.config.BuxApiConfig;
import com.alicana.btb.bux.platform.api.client.model.OpenPosition;
import com.alicana.btb.bux.platform.api.client.model.Trade;
import okhttp3.OkHttpClient;

/**
 * Implementation of Bux REST API using Retrofit.
 */
public class BuxApiRestClientImpl implements BuxApiRestClient {

  private final BuxApiRestService buxApiRestService;

  public BuxApiRestClientImpl(final OkHttpClient sharedClient, final BuxApiConfig buxApiConfig) {

    this.buxApiRestService = createService(BuxApiRestService.class, sharedClient, buxApiConfig);
  }

  @Override
  public Trade openPosition(final OpenPosition openPosition) {
    return executeSync(buxApiRestService.openPosition(openPosition));
  }

  @Override
  public Trade closePosition(String positionId) {
    return executeSync(buxApiRestService.closePosition(positionId));
  }
}
