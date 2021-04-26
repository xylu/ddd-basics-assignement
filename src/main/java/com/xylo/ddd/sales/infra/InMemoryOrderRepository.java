package com.xylo.ddd.sales.infra;

import com.xylo.ddd.sales.domain.order.Order;
import com.xylo.ddd.sales.domain.order.OrderRepository;
import com.xylo.ddd.sales.domain.order.SearchCriteria;
import com.xylo.ddd.sales.domain.order.SortOrder;
import com.xylo.ddd.shared.Identifier;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class InMemoryOrderRepository implements OrderRepository {

    private final ConcurrentMap<Identifier, Order> store = new ConcurrentHashMap<>();

    @Override
    public Order save(Order order) {
        if (order.getId() != null) {
            store.put(order.getId(), order);
            return order;
        }
        order.setId(Identifier.from(UUID.randomUUID()));
        order.getOrderLines().forEach( orderLine ->  orderLine.setId(Identifier.from(UUID.randomUUID())));
        store.put(order.getId(), order);
        return order;
    }

    @Override
    public Optional<Order> find(Identifier orderId) {
        return Optional.ofNullable(store.get(orderId));
    }

    @Override
    public boolean delete(Identifier orderId) {
        return store.remove(orderId) != null;
    }


    @Override
    public Collection<Order> find(SearchCriteria searchCriteria, SortOrder sortOrder) {
        return store.values().stream()
                .filter(searchCriteria)
                .sorted(sortOrder)
                .collect(Collectors.toList());
    }
}
