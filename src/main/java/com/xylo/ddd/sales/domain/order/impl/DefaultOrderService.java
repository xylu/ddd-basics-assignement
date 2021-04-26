package com.xylo.ddd.sales.domain.order.impl;

import com.xylo.ddd.sales.domain.order.*;
import com.xylo.ddd.shared.Identifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class DefaultOrderService implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(Identifier customerId, Collection<OrderLine> orderLines) {
        log.info("Creating order for {} customer", customerId);
        return orderRepository.save(new Order(customerId, new ArrayList<>(orderLines)));
    }

    @Override
    public Collection<Order> search(SearchCriteria searchCriteria, SortOrder sortOrder) {
        return orderRepository.find(searchCriteria, sortOrder);
    }

    @Override
    public Optional<Order> getOrder(Identifier orderId) {
        return orderRepository.find(orderId);
    }

    @Override
    public void applyDiscount(Identifier orderId, Identifier discountCode) {
        orderRepository.find(orderId)
                .ifPresentOrElse(order -> order.applyDiscount(discountCode),
                        () -> log.info("No {} order found. Discount not applied!", orderId));
    }

    @Override
    public void markOrderPaid(Identifier orderId, Identifier paymentId) {
        orderRepository.find(orderId)
                .ifPresentOrElse(order -> {
                            order.markPaid(paymentId);
                            log.info("Paid {}", order);
                        },
                        () -> log.info("No {} order found. Order state not changed!", orderId));
    }

    @Override
    public void markOrderAssembled(Identifier orderId, Identifier assembleId) {
        orderRepository.find(orderId)
                .ifPresentOrElse(order -> {
                            order.markAssembled(assembleId);
                            log.info("Assembled {}", order);
                        },
                        () -> log.info("No {} order found. Order state not changed!", orderId));
    }

    @Override
    public void markOrderDispatched(Identifier orderId, Identifier shipmentId) {
        orderRepository.find(orderId)
                .ifPresentOrElse(order -> {
                            order.markDispatched(shipmentId);
                            log.info("Dispatched {}", order);
                        },
                        () -> log.info("No {} order found. Order state not changed!", orderId));

    }

    @Override
    public void markOrderDelivered(Identifier orderId, Identifier shipmentId) {
        orderRepository.find(orderId)
                .ifPresentOrElse(order -> {
                            order.markDelivered(shipmentId);
                            log.info("Delivered & fulfilled {}", order);
                        },
                        () -> log.info("No {} order found. Order state not changed!", orderId));

    }
}
