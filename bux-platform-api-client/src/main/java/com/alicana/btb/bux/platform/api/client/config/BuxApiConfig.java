package com.alicana.btb.bux.platform.api.client.config;

/**
 * Bux API configuration record. Configuration consist of webSocket url,
 * rest endpoint url and authorization token.
 */
public record BuxApiConfig(String webSocketUrl, String restUrl, String authToken) {

}
