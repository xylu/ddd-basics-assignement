package com.xylo.ddd.shared.messaging.events;

import com.xylo.ddd.shared.Identifier;


public record OrderDeliveredEvent(Identifier orderId,
                                  Identifier shipmentId) implements OrderEvent {
}
