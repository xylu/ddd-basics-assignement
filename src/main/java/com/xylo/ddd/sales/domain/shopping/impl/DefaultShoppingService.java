package com.xylo.ddd.sales.domain.shopping.impl;

import com.xylo.ddd.sales.domain.order.OrderService;
import com.xylo.ddd.sales.domain.shopping.ShoppingCart;
import com.xylo.ddd.sales.domain.shopping.ShoppingCartRepository;
import com.xylo.ddd.sales.domain.shopping.ShoppingItem;
import com.xylo.ddd.sales.domain.shopping.ShoppingService;
import com.xylo.ddd.shared.Identifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DefaultShoppingService implements ShoppingService {

    private final ShoppingCartRepository shoppingCartRepository;

    private final OrderService orderService;


    @Override
    public void addItem(ShoppingItem item, Identifier customerId) {
        getOrCreateShoppingCart(customerId).addItem(item);
    }

    @Override
    public void removeItem(int itemNo, Identifier customerId) {
        getOrCreateShoppingCart(customerId).removeItem(itemNo);
    }

    @Override
    public Identifier checkout(Identifier customerId) {
        ShoppingCart shoppingCart = shoppingCartRepository.find(customerId).orElseThrow();
        log.info("Shopping cart checkout: {}", shoppingCart);
        return orderService.createOrder(customerId, shoppingCart.orderLines()).getId();
    }


    private ShoppingCart getOrCreateShoppingCart(Identifier customerId) {
        return shoppingCartRepository.find(customerId)
                .orElseGet(() -> shoppingCartRepository.create(customerId));
    }
}
