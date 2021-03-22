package com.alicana.btb.bux.platform.api.client;

import com.alicana.btb.bux.platform.api.client.model.OpenPosition;
import com.alicana.btb.bux.platform.api.client.model.Trade;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Bux REST API URL mappings for client generation.
 */
public interface BuxApiRestService {

  @Headers({
      "Content-type: application/json",
      "Accept: application/json"
  })
  @POST("users/me/trades")
  Call<Trade> openPosition(@Body OpenPosition openPosition);


  @Headers({
      "Content-type: application/json",
      "Accept: application/json"
  })
  @DELETE("users/me/portfolio/positions/{positionId}")
  Call<Trade> closePosition(@Path("positionId") String positionId);

}
