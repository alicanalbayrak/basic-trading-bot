package com.alicana.btb.bux.platform.api.client.interceptor;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Injects Authorization token to the headers.
 */
public record AuthenticationInterceptor(String authToken) implements Interceptor {

  private static final String AUTH_HEADER = "Authorization";
  private static final String AUTH_TOKEN_TEMPLATE = "Bearer %s";
  private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";

  @Override
  public Response intercept(Chain chain) throws IOException {

    final Request request = chain.request();

    final Request headerModifiedRequest = request.newBuilder()
        .addHeader(AUTH_HEADER, String.format(AUTH_TOKEN_TEMPLATE, authToken))
        .addHeader(ACCEPT_LANGUAGE_HEADER, "nl-NL,en;q=0.8")
        .build();

    return chain.proceed(headerModifiedRequest);
  }
}
