module bux.platform.trading.app {
  requires java.base;
  requires java.logging;
  requires java.sql;
  requires org.slf4j;
  requires org.apache.commons.configuration2;
  requires commons.beanutils;
  requires com.alicana.btb.bux.platform.api.client;
  requires com.fasterxml.jackson.annotation;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires io.reactivex.rxjava3;

  opens com.alicana.btb.bux.platform.trading.app;
  opens com.alicana.btb.bux.platform.trading.app.config;
  opens com.alicana.btb.bux.platform.trading.app.model;
  opens com.alicana.btb.bux.platform.trading.app.service.impl;
}