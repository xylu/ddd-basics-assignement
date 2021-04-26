package com.xylo.ddd.sales.domain.order;

import com.xylo.ddd.shared.Amount;
import com.xylo.ddd.shared.Identifier;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class OrderLine {

    private Identifier id;

    private final Identifier productId;

    private final String description;

    private final Amount unitPrice;

    private final Amount taxPercent;

    private final int quantity;

    public Amount netAmount() {
        return new Amount(unitPrice.getValue().doubleValue() * quantity);
    }


}
