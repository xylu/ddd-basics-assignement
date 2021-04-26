package com.xylo.ddd.shared.messaging.events;

import com.xylo.ddd.shared.Identifier;
import lombok.Value;


public record OrderDispatchedEvent(Identifier orderId,
                                   Identifier shipmentId) implements OrderEvent {
}
