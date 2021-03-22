package com.alicana.btb.bux.platform.api.client;

import com.alicana.btb.bux.platform.api.client.model.OpenPosition;
import com.alicana.btb.bux.platform.api.client.model.Trade;

/**
 * Bux API facade, supporting access Buxs REST API.
 */
public interface BuxApiRestClient {

  Trade openPosition(final OpenPosition openPosition);

  Trade closePosition(final String productId);

}
