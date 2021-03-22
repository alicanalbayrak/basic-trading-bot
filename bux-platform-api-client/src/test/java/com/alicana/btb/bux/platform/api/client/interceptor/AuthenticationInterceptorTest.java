package com.alicana.btb.bux.platform.api.client.interceptor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import okhttp3.Interceptor;
import okhttp3.Request;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

  private Request request;

  @Mock
  private Interceptor.Chain chain;

  @Captor
  private ArgumentCaptor<Request> captor;

  @BeforeEach
  void setUp() {
    request = new Request.Builder()
        .url("http://localhost:10080")
        .addHeader("default", "test")
        .build();
  }

  @Test
  void shouldExtendHeaderWithAuthToken() throws IOException {

    // GIVEN
    final String authToken = "my-auth-token";
    final AuthenticationInterceptor authenticationInterceptor
        = new AuthenticationInterceptor(authToken);

    // WHEN
    when(chain.request()).thenReturn(request);
    authenticationInterceptor.intercept(chain);

    // THEN
    verify(chain).proceed(captor.capture());
    Request interceptedRequest = captor.getValue();

    assertThat(interceptedRequest.headers().size()).isEqualTo(3);

    Set<String> headerKeys =
        new HashSet<>(Arrays.asList("default", "Authorization", "Accept-Language"));
    Assertions.assertThat(interceptedRequest.headers().names()).containsAll(headerKeys);

    assertThat(interceptedRequest.header("Authorization")).isEqualTo("Bearer my-auth-token");

  }
}