package com.xylo.ddd.sales.domain.order;

import com.xylo.ddd.shared.Amount;
import com.xylo.ddd.shared.Currency;
import com.xylo.ddd.shared.Identifier;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

@Data
public class Order {

    private Identifier id;

    private final Identifier customerId;

    private final List<OrderLine> orderLines = new ArrayList<>();

    private Amount netAmount;

    private Amount taxAmount;

    private Amount grossAmount;

    private Currency currency = Currency.EUR;

    private State state = State.NEW;

    private Identifier paymentId;

    private Identifier assembleId;

    private Identifier shipmentId;

    public Order(Identifier customerId, List<OrderLine> orderLines) {
        this.customerId = customerId;
        orderLines.forEach(this::addOrderLine);
    }

    public Collection<OrderLine> getOrderLines() {
        return List.copyOf(orderLines);
    }

    public void addOrderLine(OrderLine orderLine) {
        orderLines.add(orderLine);
        netAmount = new Amount(orderLines.stream().reduce(BigDecimal.ZERO,
                (a, b) -> a.add(b.netAmount().getValue()), BigDecimal::add));
        taxAmount = new Amount(orderLines.stream().reduce(BigDecimal.ZERO,
                (a, b) -> a.add(b.netAmount().getValue().multiply(b.getTaxPercent().getValue())),
                BigDecimal::add));
        grossAmount = new Amount(netAmount.getValue().add(taxAmount.getValue()));
    }

    public void applyDiscount(Identifier discountCode) {
        // TODO: implement
    }

    public void markPaid(Identifier paymentId) {
        checkState(state == State.NEW, "Illegal state " + state);
        checkState(this.paymentId == null, "Payment already set");
        state = State.PAID;
        this.paymentId = paymentId;
    }

    public void markAssembled(Identifier assembleId) {
        checkState(state == State.PAID, "Illegal state " + state);
        checkState(this.assembleId == null, "Assemble already set");
        state = State.ASSEMBLED;
        this.assembleId = assembleId;
    }

    public void markDispatched(Identifier shipmentId) {
        checkState(state == State.ASSEMBLED, "Illegal state " + state);
        checkState(this.shipmentId == null, "Shipment already set");
        state = State.DISPATCHED;
        this.shipmentId = shipmentId;
    }


    public void markDelivered(Identifier shipmentId) {
        checkState(state == State.DISPATCHED, "Illegal state " + state);
        checkState(this.shipmentId.equals(shipmentId), "Shipment dispatched (%s)  " +
                "does not match Shipment delivered (%s)", this.shipmentId, shipmentId);
        state = State.FULFILLED;
    }

}
