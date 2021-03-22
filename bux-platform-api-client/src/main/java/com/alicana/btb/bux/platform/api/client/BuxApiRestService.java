package com.alicana.btb.bux.platform.api.client;

import com.alicana.btb.bux.platform.api.client.model.OpenPosition;
import com.alicana.btb.bux.platform.api.client.model.Trade;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Bux REST API URL mappings for client generation.
 */
public interface BuxApiRestService {

  @POST("users/me/trades")
  Call<Trade> openPosition(@Body OpenPosition openPosition);

  @DELETE("users/me/portfolio/positions/{positionId}")
  Call<Trade> closePosition(@Path("positionId") String positionId);

}
