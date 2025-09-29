package org.iys.eventdriven.listener;

import org.apache.pulsar.client.api.SubscriptionType;
import org.iys.eventdriven.dto.OrderCreatedEvent;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

@Component
public class DebugOrderEventListener {

    @PulsarListener(
        topics = "${app.pulsar.topic.order-events}",
        subscriptionName = "debug-logger",
        subscriptionType = SubscriptionType.Shared
    )
    public void logOrderEvents(OrderCreatedEvent payload) {
        System.out.println("📥 Order Event: " + payload);
    }
}
