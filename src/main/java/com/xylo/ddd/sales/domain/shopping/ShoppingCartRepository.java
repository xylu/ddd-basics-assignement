package com.xylo.ddd.sales.domain.shopping;

import com.xylo.ddd.shared.Identifier;

import java.util.Optional;

public interface ShoppingCartRepository {

    ShoppingCart create(Identifier customerId);

    Optional<ShoppingCart> find(Identifier customerId);

}
