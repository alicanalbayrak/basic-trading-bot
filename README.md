#### Status

[![Build Status](https://travis-ci.com/alicanalbayrak/basic-trading-bot.svg?branch=main)](https://travis-ci.com/alicanalbayrak/basic-trading-bot)
[![codecov](https://codecov.io/gh/alicanalbayrak/basic-trading-bot/branch/main/graph/badge.svg?token=47TTSSXEG1)](https://codecov.io/gh/alicanalbayrak/basic-trading-bot)

## Basic Trading Bot

Very basic trading bot that tracks the price of a certain product and will execute a pre-defined trade in the said
product when it reaches a given price. After the product has been bought the trading bot should keep on tracking the
prices and execute a sell order when a certain price is hit.

#### Components overview

- bux-api-platform-api-client: Implements BUX platform client abstractions and webSocket connection protocol.
- bux-platform-trading-app: Trading orchestration.

![simple overview image](./docs/simple-overview.png "Simple Execution Overview")

### Build

#### Pre-requisites

- [Java 16](https://jdk.java.net/16/)
- [Maven](https://maven.apache.org/download.cgi) (preferably [3.6.0)+)

#### Build instructions

In the project's root directory run:

```
$ mvn clean package
```

* bux-platform-trading-app is configured to create shaded jar in its target folder.

#### Running the application

##### Application Arguments

Following arguments should provided as JSON string to the application. The rationale behind forcing user to pass the
JSON is, apart from easier validation, to provide seamless integration with serverless environments such AWS Lambda,
Azure Functions. etc.

* The product id
* The buy price. If the stock price doesn't reach that price the position shouldn't be opened.
* The upper limit sell price. This is the price you are willing to close a position and make a profit.
* The lower limit sell price. This the price you want are willing to close a position at and make a loss.

```
$ java --enable-preview -jar bux-platform-trading-app-1.0-SNAPSHOT.jar "{ \"productId\":\"sb26502\", \"buyPrice\": \"1.18231\", \"upperSellLimit\": \"1.18242\", \"lowerSellLimit\": \"1.18230\" }"
```

* **Since this project heavily uses Records `--enable-preview` argument is super important.**

#### Sample execution logs

```bash
$> java --enable-preview -jar  bux-platform-trading-app-1.0-SNAPSHOT.jar  "{ \"productId\":\"sb26496\", \"buyPrice\": \"2380.08\", \"upperSellLimit\": \"2371.80\", \"lowerSellLimit\": \"2394.80\" }"
INFO  c.a.b.b.platform.trading.app.AppMain - Creating bot instance...
INFO  c.a.b.b.p.t.app.BasicTradingBot - Input=TradingBotInputParams[productId=sb26496, buyPrice=2365.65, upperSellLimit=2370.01, lowerSellLimit=2350.80]
INFO  c.a.b.b.p.t.a.c.BuxApiClientConfigurationProvider - Reading configurations from config.properties ...
Mar 25, 2021 8:47:52 AM org.apache.commons.beanutils.FluentPropertyBeanIntrospector introspect
INFO: Error when creating PropertyDescriptor for public final void org.apache.commons.configuration2.AbstractConfiguration.setProperty(java.lang.String,java.lang.Object)! Ignoring this property.
INFO  c.a.b.b.p.a.c.a.i.ReactiveWebSocketListenerImpl - Reactive web socket listener created.
INFO  okhttp3.OkHttpClient - --> GET http://localhost:8080/subscriptions/me
INFO  okhttp3.OkHttpClient - Upgrade: websocket
INFO  okhttp3.OkHttpClient - Connection: Upgrade
INFO  okhttp3.OkHttpClient - Sec-WebSocket-Key: dcu6HSZzLeMeqKxFVGe4UA==
INFO  okhttp3.OkHttpClient - Sec-WebSocket-Version: 13
INFO  okhttp3.OkHttpClient - Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJyZWZyZXNoYWJsZSI6ZmFsc2UsInN1YiI6ImJiMGNkYTJiLWExMGUtNGVkMy1hZDVhLTBmODJiNGMxNTJjNCIsImF1ZCI6ImJldGEuZ2V0YnV4LmNvbSIsInNjcCI6WyJhcHA6bG9naW4iLCJydGY6bG9naW4iXSwiZXhwIjoxODIwODQ5Mjc5LCJpYXQiOjE1MDU0ODkyNzksImp0aSI6ImI3MzlmYjgwLTM1NzUtNGIwMS04NzUxLTMzZDFhNGRjOGY5MiIsImNpZCI6Ijg0NzM2MjI5MzkifQ.M5oANIi2nBtSfIfhyUMqJnex-JYg6Sm92KPYaUL9GKg
INFO  okhttp3.OkHttpClient - Accept-Language: nl-NL,en;q=0.8
INFO  okhttp3.OkHttpClient - --> END GET
INFO  c.a.b.b.p.a.c.BuxApiServiceGenerator - Generating REST client...
INFO  okhttp3.OkHttpClient - <-- 101 Switching Protocols http://localhost:8080/subscriptions/me (18ms)
INFO  okhttp3.OkHttpClient - Date: Thu, 25 Mar 2021 07:47:53 GMT
INFO  okhttp3.OkHttpClient - Connection: Upgrade
INFO  okhttp3.OkHttpClient - Sec-WebSocket-Accept: Kj70bmkKNktzYYwoPqLATHxyrYA=
INFO  okhttp3.OkHttpClient - Server: Jetty(9.4.z-SNAPSHOT)
INFO  okhttp3.OkHttpClient - Upgrade: WebSocket
INFO  okhttp3.OkHttpClient - <-- END HTTP
INFO  c.a.b.b.p.a.c.a.i.ReactiveWebSocketListenerImpl - WebSocket is opened
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - ConnectedEvent[userId=bb0cda2b-a10e-4ed3-ad5a-0f82b4c152c4, sessionId=d3c88c1d-111d-498c-8928-36ba1816aa0a, dt=2021-03-25T08:47:53.041+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - BuxSubscription[subscribeTo=null, unsubscribeFrom=null]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2353.11, timeStamp=2021-03-25T08:47:53.939+01:00]
INFO  okhttp3.OkHttpClient - --> POST http://localhost:8080/core/21/users/me/trades
INFO  okhttp3.OkHttpClient - Content-Type: application/json
INFO  okhttp3.OkHttpClient - Content-Length: 169
INFO  okhttp3.OkHttpClient - Accept: application/json
INFO  okhttp3.OkHttpClient - Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJyZWZyZXNoYWJsZSI6ZmFsc2UsInN1YiI6ImJiMGNkYTJiLWExMGUtNGVkMy1hZDVhLTBmODJiNGMxNTJjNCIsImF1ZCI6ImJldGEuZ2V0YnV4LmNvbSIsInNjcCI6WyJhcHA6bG9naW4iLCJydGY6bG9naW4iXSwiZXhwIjoxODIwODQ5Mjc5LCJpYXQiOjE1MDU0ODkyNzksImp0aSI6ImI3MzlmYjgwLTM1NzUtNGIwMS04NzUxLTMzZDFhNGRjOGY5MiIsImNpZCI6Ijg0NzM2MjI5MzkifQ.M5oANIi2nBtSfIfhyUMqJnex-JYg6Sm92KPYaUL9GKg
INFO  okhttp3.OkHttpClient - Accept-Language: nl-NL,en;q=0.8
INFO  okhttp3.OkHttpClient - 
INFO  okhttp3.OkHttpClient - {"productId":"sb26496","investingAmount":{"currency":"BUX","decimals":2,"amount":2353.11},"leverage":1,"direction":"BUY","source":{"sourceType":"OTHER","sourceId":null}}
INFO  okhttp3.OkHttpClient - --> END POST (169-byte body)
INFO  okhttp3.OkHttpClient - <-- 200 OK http://localhost:8080/core/21/users/me/trades (3ms)
INFO  okhttp3.OkHttpClient - Date: Thu, 25 Mar 2021 07:47:53 GMT
INFO  okhttp3.OkHttpClient - Content-Type: application/json
INFO  okhttp3.OkHttpClient - Content-Length: 505
INFO  okhttp3.OkHttpClient - Server: Jetty(9.4.z-SNAPSHOT)
INFO  okhttp3.OkHttpClient - 
INFO  okhttp3.OkHttpClient - {"id":"7dc4f68b-5596-4b5d-a870-38bbd76c6541","positionId":"d548a6c8-ad7d-4aad-b909-bc09cc518e7c","product":{"securityId":"sb26496","symbol":"US500","displayName":"US 500","currentPrice":{"currency":"USD","decimals":2,"amount":"2353.11"},"closingPrice":{"currency":"USD","decimals":2,"amount":"2500.97"}},"investingAmount":{"currency":"BUX","decimals":2,"amount":"2353.11"},"price":{"currency":"USD","decimals":2,"amount":"2353.11"},"leverage":1,"direction":"BUY","type":"OPEN","dateCreated":1616658473973}
INFO  okhttp3.OkHttpClient - <-- END HTTP (505-byte body)
INFO  c.a.b.b.p.t.a.s.i.BuxTradeServiceImpl - Open position result: Trade[id=7dc4f68b-5596-4b5d-a870-38bbd76c6541, positionId=d548a6c8-ad7d-4aad-b909-bc09cc518e7c, profitAndLoss=null, product=Product[securityId=sb26496, symbol=US500, displayName=US 500], investingAmount=BigMoney[currency=BUX, decimalPlaces=2, amount=2353.11], price=BigMoney[currency=USD, decimalPlaces=2, amount=2353.11], leverage=1, direction=BUY, type=OPEN]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2352.72, timeStamp=2021-03-25T08:47:54.841+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2352.05, timeStamp=2021-03-25T08:47:55.768+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2354.01, timeStamp=2021-03-25T08:47:55.782+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2355.71, timeStamp=2021-03-25T08:47:56.718+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2354.42, timeStamp=2021-03-25T08:47:57.664+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2352.26, timeStamp=2021-03-25T08:47:58.624+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2350.94, timeStamp=2021-03-25T08:47:59.124+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2352.64, timeStamp=2021-03-25T08:47:59.904+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2351.48, timeStamp=2021-03-25T08:48:00.389+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2351.51, timeStamp=2021-03-25T08:48:00.809+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2351.76, timeStamp=2021-03-25T08:48:01.479+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2353.59, timeStamp=2021-03-25T08:48:01.797+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2352.55, timeStamp=2021-03-25T08:48:02.744+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2354.38, timeStamp=2021-03-25T08:48:03.488+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2355.21, timeStamp=2021-03-25T08:48:03.711+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2355.32, timeStamp=2021-03-25T08:48:04.254+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2355.90, timeStamp=2021-03-25T08:48:04.973+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2356.87, timeStamp=2021-03-25T08:48:05.298+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2359.20, timeStamp=2021-03-25T08:48:05.466+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2360.40, timeStamp=2021-03-25T08:48:06.216+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2360.09, timeStamp=2021-03-25T08:48:06.709+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2359.94, timeStamp=2021-03-25T08:48:07.247+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2358.46, timeStamp=2021-03-25T08:48:07.546+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2356.84, timeStamp=2021-03-25T08:48:07.955+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2357.05, timeStamp=2021-03-25T08:48:08.952+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2355.60, timeStamp=2021-03-25T08:48:09.501+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2356.36, timeStamp=2021-03-25T08:48:09.958+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2354.64, timeStamp=2021-03-25T08:48:10.552+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2354.55, timeStamp=2021-03-25T08:48:11.482+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2353.32, timeStamp=2021-03-25T08:48:11.802+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2352.37, timeStamp=2021-03-25T08:48:12.508+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2353.28, timeStamp=2021-03-25T08:48:13.431+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2355.22, timeStamp=2021-03-25T08:48:13.906+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2354.34, timeStamp=2021-03-25T08:48:14.501+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2356.04, timeStamp=2021-03-25T08:48:15.344+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2356.02, timeStamp=2021-03-25T08:48:15.456+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2356.26, timeStamp=2021-03-25T08:48:16.152+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2354.53, timeStamp=2021-03-25T08:48:16.248+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2356.07, timeStamp=2021-03-25T08:48:16.366+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2354.02, timeStamp=2021-03-25T08:48:17.197+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2352.90, timeStamp=2021-03-25T08:48:17.911+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2353.11, timeStamp=2021-03-25T08:48:18.212+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2351.26, timeStamp=2021-03-25T08:48:19.189+01:00]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - QuoteEvent[securityId=sb26496, currentPrice=2350.35, timeStamp=2021-03-25T08:48:19.557+01:00]
INFO  c.a.b.b.p.t.a.s.i.SimpleTradeStrategy - Price hit! Closing position. Current=2350.35, Lower=2350.80, Upper=2370.01
INFO  okhttp3.OkHttpClient - --> DELETE http://localhost:8080/core/21/users/me/portfolio/positions/d548a6c8-ad7d-4aad-b909-bc09cc518e7c
INFO  okhttp3.OkHttpClient - Accept: application/json
INFO  okhttp3.OkHttpClient - Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJyZWZyZXNoYWJsZSI6ZmFsc2UsInN1YiI6ImJiMGNkYTJiLWExMGUtNGVkMy1hZDVhLTBmODJiNGMxNTJjNCIsImF1ZCI6ImJldGEuZ2V0YnV4LmNvbSIsInNjcCI6WyJhcHA6bG9naW4iLCJydGY6bG9naW4iXSwiZXhwIjoxODIwODQ5Mjc5LCJpYXQiOjE1MDU0ODkyNzksImp0aSI6ImI3MzlmYjgwLTM1NzUtNGIwMS04NzUxLTMzZDFhNGRjOGY5MiIsImNpZCI6Ijg0NzM2MjI5MzkifQ.M5oANIi2nBtSfIfhyUMqJnex-JYg6Sm92KPYaUL9GKg
INFO  okhttp3.OkHttpClient - Accept-Language: nl-NL,en;q=0.8
INFO  okhttp3.OkHttpClient - --> END DELETE
INFO  okhttp3.OkHttpClient - <-- 200 OK http://localhost:8080/core/21/users/me/portfolio/positions/d548a6c8-ad7d-4aad-b909-bc09cc518e7c (2ms)
INFO  okhttp3.OkHttpClient - Date: Thu, 25 Mar 2021 07:48:19 GMT
INFO  okhttp3.OkHttpClient - Content-Type: application/json
INFO  okhttp3.OkHttpClient - Content-Length: 572
INFO  okhttp3.OkHttpClient - Server: Jetty(9.4.z-SNAPSHOT)
INFO  okhttp3.OkHttpClient - 
INFO  okhttp3.OkHttpClient - {"id":"50c66002-d610-4d75-94d2-9b5e83ebc032","positionId":"d548a6c8-ad7d-4aad-b909-bc09cc518e7c","profitAndLoss":{"currency":"BUX","decimals":2,"amount":"-2.76"},"product":{"securityId":"sb26496","symbol":"US500","displayName":"US 500","currentPrice":{"currency":"USD","decimals":2,"amount":"2350.35"},"closingPrice":{"currency":"USD","decimals":2,"amount":"2500.97"}},"investingAmount":{"currency":"BUX","decimals":2,"amount":"2353.11"},"price":{"currency":"USD","decimals":2,"amount":"2350.35"},"leverage":1,"direction":"SELL","type":"CLOSE","dateCreated":1616658499564}
INFO  okhttp3.OkHttpClient - <-- END HTTP (572-byte body)
INFO  c.a.b.b.p.t.a.s.i.BuxTradeServiceImpl - Close position result: BigMoney[currency=BUX, decimalPlaces=2, amount=-2.76]
INFO  c.a.b.b.p.t.a.s.i.BuxMarketDataStreamingServiceImpl - Trade execution finished. Stopping market data stream.
```