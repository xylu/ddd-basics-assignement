package com.xylo.ddd.shared.messaging.events;

import com.xylo.ddd.shared.Identifier;

public record OrderPaidEvent(Identifier orderId,
                             Identifier paymentId) implements OrderEvent {
}
