package com.alicana.btb.bux.platform.api.client.model;

/**
 * When received unknown event type, this record used to log uknown event.
 */
public final record UnknownEvent(String eventAsString) implements BuxDomainEvent {
}
