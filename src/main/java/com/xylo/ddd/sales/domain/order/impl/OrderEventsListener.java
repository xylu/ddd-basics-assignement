package com.xylo.ddd.sales.domain.order.impl;

import com.xylo.ddd.sales.domain.order.OrderService;
import com.xylo.ddd.shared.messaging.EventListener;
import com.xylo.ddd.shared.messaging.events.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OrderEventsListener implements EventListener<OrderEvent> {


    private final OrderService orderService;

    @Override
    public void onEvent(OrderEvent event) {

        if (event instanceof OrderPaidEvent orderPaidEvent) {
            orderService.markOrderPaid(orderPaidEvent.orderId(), orderPaidEvent.paymentId());
        } else if (event instanceof OrderAssembledEvent orderAssembledEvent) {
            orderService.markOrderAssembled(orderAssembledEvent.orderId(), orderAssembledEvent.assembleId());
        } else if (event instanceof OrderDispatchedEvent orderDispatchedEvent) {
            orderService.markOrderDispatched(orderDispatchedEvent.orderId(), orderDispatchedEvent.shipmentId());
        } else if (event instanceof OrderDeliveredEvent orderDeliveredEvent) {
            orderService.markOrderDelivered(orderDeliveredEvent.orderId(), orderDeliveredEvent.shipmentId());
        } else {
            log.warn("Unknown event:{}", event);
        }

        /* NEW Switch

        final String aClass = event.getClass().getSimpleName();
        switch (aClass) {
            case "OrderPaidEvent" -> {
                OrderPaidEvent orderPaidEvent = (OrderPaidEvent) event;
                orderService.markOrderPaid(orderPaidEvent.getOrderId(), orderPaidEvent.getPaymentId());
            }
        }*/
    }

}
