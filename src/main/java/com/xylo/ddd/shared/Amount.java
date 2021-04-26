package com.xylo.ddd.shared;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Amount {
    BigDecimal value;

    public Amount(double value) {
        this.value = BigDecimal.valueOf(value);
    }

    public Amount(BigDecimal value) {
        this.value = value;
    }
}
