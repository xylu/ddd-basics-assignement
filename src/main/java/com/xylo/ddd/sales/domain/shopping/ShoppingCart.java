package com.xylo.ddd.sales.domain.shopping;

import com.xylo.ddd.sales.domain.order.OrderLine;
import com.xylo.ddd.shared.Amount;
import com.xylo.ddd.shared.Currency;
import com.xylo.ddd.shared.Identifier;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
public class ShoppingCart {

    private Amount netAmount;

    private Amount taxAmount;

    private Amount grossAmount;

    private Currency currency = Currency.EUR;

    private final Identifier customerId;

    @Getter(AccessLevel.NONE)
    private final List<ShoppingItem> items = new ArrayList<>();

    public void addItem(ShoppingItem item) {
        items.add(item);

        netAmount = new Amount(items.stream().reduce(BigDecimal.ZERO,
                (a, b) -> a.add(b.netAmount().getValue()), BigDecimal::add));
        taxAmount = new Amount(items.stream().reduce(BigDecimal.ZERO,
                (a, b) -> a.add(b.netAmount().getValue().multiply(b.getTaxPercent().getValue())),
                BigDecimal::add));
        grossAmount = new Amount(netAmount.getValue().add(taxAmount.getValue()));
        log.info("Added item {} to customer {} shopping cart", item, customerId);
    }

    public void removeItem(int itemNo) {
        items.remove(itemNo);
    }

    public Collection<OrderLine> orderLines() {
        return items.stream().map(ShoppingItem::toOrderLine).collect(Collectors.toList());
    }

}
