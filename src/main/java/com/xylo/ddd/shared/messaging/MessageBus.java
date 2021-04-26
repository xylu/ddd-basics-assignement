package com.xylo.ddd.shared.messaging;

public interface MessageBus {

    <T extends Event> void register(EventListener<? super T> eventListener, Class<T> eventClass);

    <T extends Event> void unregister(EventListener<? super T> eventListener, Class<T> eventClass);

    <T extends Event> void publish(T event);

}
