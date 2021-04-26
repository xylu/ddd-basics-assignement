package com.xylo.ddd.shared.messaging;

public interface EventListener<T extends Event> {

    void onEvent(T event);
}
