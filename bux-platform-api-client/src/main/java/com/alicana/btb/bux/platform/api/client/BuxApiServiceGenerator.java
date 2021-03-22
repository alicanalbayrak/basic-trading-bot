package com.alicana.btb.bux.platform.api.client;

import com.alicana.btb.bux.platform.api.client.exception.BuxApiException;
import com.alicana.btb.bux.platform.api.client.model.BuxRestError;
import com.alicana.btb.bux.platform.api.client.config.BuxApiConfig;
import com.alicana.btb.bux.platform.api.client.interceptor.AuthenticationInterceptor;
import com.alicana.btb.bux.platform.api.client.interceptor.LoggingInterceptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Generates a Bux API implementation based on @see {@link BuxApiRestService}.
 */
public class BuxApiServiceGenerator {

  private static final Converter.Factory converterFactory = JacksonConverterFactory.create();

  @SuppressWarnings({"checkstyle:WhitespaceAfter", "unchecked"})
  private static final Converter<ResponseBody, BuxRestError> errorBodyConverter =
      (Converter<ResponseBody, BuxRestError>) converterFactory.responseBodyConverter(
          BuxRestError.class, new Annotation[0], null);

  /**
   * Provides implementation for the endpoint mapping interface.
   *
   * @param serviceClass Bux Api URL mapper interface
   * @param sharedClient HTTP client with shared thread pool
   * @param buxApiConfig The Bux API configuration
   * @param <S>          Type of the service
   * @return Implementation of the API endpoints defined by the service interface
   */
  public static <S> S createService(final Class<S> serviceClass,
                                    final OkHttpClient sharedClient,
                                    final BuxApiConfig buxApiConfig) {

    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
        .baseUrl(buxApiConfig.restUrl())
        .addConverterFactory(converterFactory);

    AuthenticationInterceptor interceptor = new AuthenticationInterceptor(buxApiConfig.authToken());
    OkHttpClient httpClient = sharedClient.newBuilder()
        .addInterceptor(new LoggingInterceptor())
        .addInterceptor(interceptor)
        .build();

    retrofitBuilder.client(httpClient);

    Retrofit retrofit = retrofitBuilder.build();
    return retrofit.create(serviceClass);
  }

  /**
   * Execute a REST call and block until the response is received.
   */
  public static <T> T executeSync(Call<T> call) {
    try {
      Response<T> response = call.execute();
      if (response.isSuccessful()) {
        return response.body();
      } else {
        BuxRestError apiError = getBuxApiError(response);
        throw new BuxApiException(apiError);
      }
    } catch (IOException e) {
      throw new BuxApiException(e);
    }
  }

  /**
   * Extracts and converts the response error body into an object.
   */
  public static BuxRestError getBuxApiError(Response<?> response)
      throws IOException, BuxApiException {
    return errorBodyConverter.convert(response.errorBody());
  }

}
