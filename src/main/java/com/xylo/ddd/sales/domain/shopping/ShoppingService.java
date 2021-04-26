package com.xylo.ddd.sales.domain.shopping;

import com.xylo.ddd.shared.Identifier;

public interface ShoppingService {

    void addItem(ShoppingItem item, Identifier customerId);

    void removeItem(int itemNo, Identifier customerId);

    /**
     * @param customerId customer id that shopping cart belongs to
     * @return order id  for items present in customer shopping cart
     */
    Identifier checkout(Identifier customerId);
}
