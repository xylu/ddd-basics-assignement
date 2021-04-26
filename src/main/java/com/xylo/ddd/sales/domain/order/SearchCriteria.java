package com.xylo.ddd.sales.domain.order;

import java.util.function.Predicate;

public interface SearchCriteria extends Predicate<Order> {
}
