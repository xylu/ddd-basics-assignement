package com.xylo.ddd.sales.domain.shopping;

import com.xylo.ddd.sales.domain.order.OrderLine;
import com.xylo.ddd.shared.Amount;
import com.xylo.ddd.shared.Identifier;
import lombok.Data;

import java.util.UUID;

@Data
public class ShoppingItem {

    private final String name;

    private final Identifier productId;

    private final Amount unitPrice;

    private final Amount taxPercent;

    private final int quantity;

    public OrderLine toOrderLine() {
        return new OrderLine(productId, name, unitPrice, taxPercent, quantity);

    }
    public Amount netAmount() {
        return new Amount(unitPrice.getValue().doubleValue() * quantity);
    }

}
