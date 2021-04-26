package com.xylo.ddd.sales.infra;

import com.xylo.ddd.shared.messaging.Event;
import com.xylo.ddd.shared.messaging.EventListener;
import com.xylo.ddd.shared.messaging.MessageBus;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryMessageBus implements MessageBus {


    private ConcurrentMap<Class<? extends Event>, Collection<EventListener<?>>> listenersByEventType = new ConcurrentHashMap<>();

    @Override
    public <T extends Event> void register(EventListener<? super T> eventListener, Class<T> eventClass) {
        listenersByEventType.computeIfAbsent(eventClass, eventClazz -> ConcurrentHashMap.newKeySet())
                .add(eventListener);
    }

    @Override
    public <T extends Event> void unregister(EventListener<? super T> eventListener, Class<T> eventClass) {
        Optional.ofNullable(listenersByEventType.get(eventClass)).ifPresent(eventListeners -> eventListeners.remove(eventListener));
    }

    @Override
    public <T extends Event> void publish(T event) {

        Optional.ofNullable(listenersByEventType.get(event.getClass()))
                .ifPresent(eventListeners ->
                        eventListeners.forEach(eventListener ->
                                ((EventListener<T>) eventListener).onEvent(event)));

    }


}
