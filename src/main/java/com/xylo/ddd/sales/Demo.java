package com.xylo.ddd.sales;

import com.google.common.base.Preconditions;
import com.xylo.ddd.sales.domain.order.Order;
import com.xylo.ddd.sales.domain.order.OrderService;
import com.xylo.ddd.sales.domain.order.SortOrder;
import com.xylo.ddd.sales.domain.order.State;
import com.xylo.ddd.sales.domain.order.impl.DefaultOrderService;
import com.xylo.ddd.sales.domain.order.impl.OrderEventsListener;
import com.xylo.ddd.sales.domain.shopping.ShoppingItem;
import com.xylo.ddd.sales.domain.shopping.ShoppingService;
import com.xylo.ddd.sales.domain.shopping.impl.DefaultShoppingService;
import com.xylo.ddd.sales.infra.InMemoryMessageBus;
import com.xylo.ddd.sales.infra.InMemoryOrderRepository;
import com.xylo.ddd.sales.infra.InMemoryShoppingCartRepository;
import com.xylo.ddd.shared.Amount;
import com.xylo.ddd.shared.Identifier;
import com.xylo.ddd.shared.messaging.MessageBus;
import com.xylo.ddd.shared.messaging.events.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkState;

@Slf4j
public class Demo {

    public static void main(String[] args) {

        Identifier customerId = Identifier.from(UUID.randomUUID());
        OrderService orderService = new DefaultOrderService(new InMemoryOrderRepository());
        ShoppingService shoppingService = new DefaultShoppingService(new InMemoryShoppingCartRepository(), orderService);

        MessageBus messageBus = new InMemoryMessageBus();
        OrderEventsListener orderEventsListener = new OrderEventsListener(orderService);
        messageBus.register(orderEventsListener, OrderPaidEvent.class);
        messageBus.register(orderEventsListener, OrderAssembledEvent.class);
        messageBus.register(orderEventsListener, OrderDispatchedEvent.class);
        messageBus.register(orderEventsListener, OrderDeliveredEvent.class);


        log.info("Demo for customer {} shopping", customerId);
        ShoppingItem t_Shirt = new ShoppingItem("T-Shirt", Identifier.from(UUID.randomUUID()), new Amount(5.0), new Amount(0.10), 2);
        ShoppingItem trousers = new ShoppingItem("Trousers", Identifier.from(UUID.randomUUID()), new Amount(15.0), new Amount(0.10), 1);
        shoppingService.addItem(t_Shirt, customerId);
        shoppingService.addItem(trousers, customerId);
        Identifier orderId = shoppingService.checkout(customerId);
        log.info("Fetching order by {}", orderId);
        orderService.getOrder(orderId).ifPresentOrElse(
                order -> log.info("Found order: {}", order),
                () -> {
                    throw new IllegalStateException("Order not found");
                }
        );

        Identifier paymentId = Identifier.from(UUID.randomUUID());
        Identifier assembleId = Identifier.from(UUID.randomUUID());
        Identifier shipmentId = Identifier.from(UUID.randomUUID());

        Predicate<Order> isTheOrder = o -> orderId.equals(o.getId());
        SortOrder sortOrder = (o1, o2) -> o1.getId().compareTo(o2.getId());

        messageBus.publish(new OrderPaidEvent(orderId, paymentId));
        checkState(orderService.search(o -> o.getState() == State.PAID, sortOrder)
                .stream().filter(isTheOrder).count() == 1, "PAID event not handled");

        messageBus.publish(new OrderAssembledEvent(orderId, assembleId));
        checkState(orderService.search(o -> o.getState() == State.ASSEMBLED, sortOrder)
                .stream().filter(isTheOrder).count() == 1, "ASSEMBLED event not handled");

        messageBus.publish(new OrderDispatchedEvent(orderId, shipmentId));
        checkState(orderService.search(o -> o.getState() == State.DISPATCHED, sortOrder)
                .stream().filter(isTheOrder).count() == 1, "DISPATCHED event not handled");

        messageBus.publish(new OrderDeliveredEvent(orderId, shipmentId));
        checkState(orderService.search(o -> o.getState() == State.FULFILLED, sortOrder)
                .stream().filter(isTheOrder).count() == 1, "DELIVERED event not handled");


    }
}
