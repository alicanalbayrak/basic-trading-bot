module bux.platform.trading.app {
  requires java.base;
  requires java.logging;
  requires java.sql;
  requires org.slf4j;
  requires org.apache.commons.configuration2;
  requires commons.beanutils;
  requires com.alicana.btb.bux.platform.api.client;

  opens com.alicana.btb.bux.platform.trading.app.config;
}