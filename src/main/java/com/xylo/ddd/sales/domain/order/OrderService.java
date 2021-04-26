package com.xylo.ddd.sales.domain.order;

import com.xylo.ddd.shared.Identifier;

import java.util.Collection;
import java.util.Optional;

public interface OrderService {


    Order createOrder(Identifier customerId, Collection<OrderLine> orderLines);

    Optional<Order> getOrder(Identifier orderId);

    Collection<Order> search(SearchCriteria searchCriteria, SortOrder sortOrder);

    void applyDiscount(Identifier orderId, Identifier discountCode);

    void markOrderPaid(Identifier orderId, Identifier paymentId);

    void markOrderAssembled(Identifier orderId, Identifier assembleId);

    void markOrderDispatched(Identifier orderId, Identifier shipmentId);

    void markOrderDelivered(Identifier orderId, Identifier shipmentId);



}
