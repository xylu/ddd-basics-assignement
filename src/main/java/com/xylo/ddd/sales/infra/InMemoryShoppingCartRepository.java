package com.xylo.ddd.sales.infra;

import com.xylo.ddd.sales.domain.shopping.ShoppingCart;
import com.xylo.ddd.sales.domain.shopping.ShoppingCartRepository;
import com.xylo.ddd.shared.Identifier;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryShoppingCartRepository implements ShoppingCartRepository {

    private final ConcurrentMap<Identifier, ShoppingCart> store = new ConcurrentHashMap<>();


    @Override
    public ShoppingCart create(Identifier customerId) {
        ShoppingCart shoppingCart = new ShoppingCart(customerId);
        store.put(customerId, shoppingCart);
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> find(Identifier customerId) {
        return Optional.ofNullable(store.get(customerId));
    }
}
