package com.xylo.ddd.sales.domain.order;

import com.xylo.ddd.shared.Identifier;

import java.util.Collection;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> find(Identifier orderId);

    boolean delete(Identifier orderId);

    Collection<Order> find(SearchCriteria searchCriteria, SortOrder sortOrder);

}
