package com.alicana.btb.bux.platform.trading.app.config;

import com.alicana.btb.bux.platform.api.client.config.BuxApiConfig;
import com.alicana.btb.bux.platform.trading.app.exception.TradingBotException;
import java.io.File;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads BUX platform API Client configuration from a properties file.
 */
public class BuxApiClientConfigurationProvider {

  private static final Logger log =
      LoggerFactory.getLogger(BuxApiClientConfigurationProvider.class);

  private static final String propertiesFileName = "config.properties";
  private static final String webSocketUrlPropertyName = "bux.platform.api.client.webSocket.url";
  private static final String restUrlPropertyName = "bux.platform.api.client.rest.url";
  private static final String authorizationTokenPropertyName =
      "bux.platform.api.client.authorization.token";

  /**
   * Private constructor.
   */
  private BuxApiClientConfigurationProvider() {

  }

  /**
   * Reads default configuration file in the classpath and provides.
   *
   * @return The Bux API configuration
   */
  public static BuxApiConfig readConfigurations() {
    return readConfigurations(propertiesFileName);
  }

  /**
   * Reads configuration file in the classpath and provides BuxApiConfig.
   *
   * @param propertiesFileName Custom file (e.g. test config).
   * @return The Bux API configuration.
   */
  public static BuxApiConfig readConfigurations(final String propertiesFileName) {

    log.info("Reading configurations from {} ...", propertiesFileName);

    File file = new File(propertiesFileName);

    PropertiesBuilderParameters propertyParameters = new Parameters().properties();
    propertyParameters.setFile(file);
    propertyParameters.setThrowExceptionOnMissing(true);

    FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
        new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class);

    try {

      PropertiesConfiguration propertiesConfiguration =
          builder.configure(propertyParameters).getConfiguration();

      final String buxWebSocketUrl = propertiesConfiguration.getString(webSocketUrlPropertyName);
      final String buxRestUrl = propertiesConfiguration.getString(restUrlPropertyName);
      final String buxAuthorizationToken =
          propertiesConfiguration.getString(authorizationTokenPropertyName);

      return new BuxApiConfig(buxWebSocketUrl, buxRestUrl, buxAuthorizationToken);

    } catch (ConfigurationException e) {
      log.error("Something went wrong while reading configuration", e);
      throw new TradingBotException("Something went wrong while configuring");
    }
  }


}
