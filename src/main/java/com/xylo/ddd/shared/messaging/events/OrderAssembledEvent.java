package com.xylo.ddd.shared.messaging.events;

import com.xylo.ddd.shared.Identifier;
import lombok.Value;

public record OrderAssembledEvent(Identifier orderId,
                                  Identifier assembleId) implements OrderEvent {
}
