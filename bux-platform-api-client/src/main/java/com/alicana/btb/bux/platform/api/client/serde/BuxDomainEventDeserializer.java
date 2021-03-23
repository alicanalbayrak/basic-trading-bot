package com.alicana.btb.bux.platform.api.client.serde;

import com.alicana.btb.bux.platform.api.client.model.BuxDomainEvent;
import com.alicana.btb.bux.platform.api.client.model.BuxSubscription;
import com.alicana.btb.bux.platform.api.client.model.ConnectedEvent;
import com.alicana.btb.bux.platform.api.client.model.FailureEvent;
import com.alicana.btb.bux.platform.api.client.model.QuoteEvent;
import com.alicana.btb.bux.platform.api.client.model.UnknownEvent;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deserializes BUX API WebSocket events.
 */
public class BuxDomainEventDeserializer extends JsonDeserializer<BuxDomainEvent> {

  private final static Logger log = LoggerFactory.getLogger(BuxDomainEventDeserializer.class);

  @Override
  public BuxDomainEvent deserialize(JsonParser jsonParser,
                                    DeserializationContext deserializationContext)
      throws IOException {

    ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
    JsonNode rootNode = mapper.readTree(jsonParser);

    if (!rootNode.has("t")) {
      if (rootNode.has("subscribeTo") || rootNode.has("SUBSCRIBETO")) {
        return mapper.readValue(rootNode.toString(), BuxSubscription.class);
      }

      log.warn("Received event from WebSocket but cannot infer the type of the event. Event: {}",
          rootNode);
      return new UnknownEvent(rootNode.toString());
    }

    String bodyJsonString = rootNode.get("body").toString();

    return switch (rootNode.get("t").asText()) {
      case "connect.connected" -> mapper.readValue(bodyJsonString, ConnectedEvent.class);
      case "connect.failed" -> mapper.readValue(bodyJsonString, FailureEvent.class);
      case "trading.quote" -> mapper.readValue(bodyJsonString, QuoteEvent.class);
      default -> new UnknownEvent(rootNode.toString());
    };
  }
}
