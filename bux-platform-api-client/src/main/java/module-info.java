module com.alicana.btb.bux.platform.api.client {
  requires java.base;
  requires java.logging;
  requires org.slf4j;
  requires com.fasterxml.jackson.annotation;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires org.joda.time;
  requires org.reactivestreams;
  requires io.reactivex.rxjava3;
  requires retrofit2;
  requires retrofit2.converter.jackson;
  requires okhttp3;
  requires okhttp3.logging;
  requires okio;

  opens com.alicana.btb.bux.platform.api.client;
  opens com.alicana.btb.bux.platform.api.client.adapter;
  opens com.alicana.btb.bux.platform.api.client.adapter.impl;
  opens com.alicana.btb.bux.platform.api.client.config;
  opens com.alicana.btb.bux.platform.api.client.model;
  opens com.alicana.btb.bux.platform.api.client.serde;
  opens com.alicana.btb.bux.platform.api.client.interceptor;
  opens com.alicana.btb.bux.platform.api.client.exception;

  exports com.alicana.btb.bux.platform.api.client;
  exports com.alicana.btb.bux.platform.api.client.config;
  exports com.alicana.btb.bux.platform.api.client.model;
}