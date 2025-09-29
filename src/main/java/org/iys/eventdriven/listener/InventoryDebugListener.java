// package: org.iys.eventdriven.listener
package org.iys.eventdriven.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.SubscriptionType;
import org.iys.eventdriven.dto.event.OrderCreatedEvent;
import org.iys.eventdriven.dto.event.OrderedProcessedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryDebugListener {

  private final PulsarTemplate<OrderedProcessedEvent> pulsarTemplate;

  @Value("${app.pulsar.topic.order-processed-events}")
  private String orderTopic;

  @PulsarListener(
      topics = "#{@pulsarTopicProps.orderEvents}",
      subscriptionName = "inventory-svc",
      subscriptionType = SubscriptionType.Key_Shared
  )
  public String onOrder(OrderCreatedEvent e) {
    log.info("📥 Order received -> id={} sku={} qty={} customer={}",
        e.orderId(), e.sku(), e.quantity(), e.customerId());

    OrderedProcessedEvent event = OrderedProcessedEvent.builder().status("Proccessed").orderId(e.orderId()).customerId(e.customerId()).build();

    pulsarTemplate.newMessage(event)
            .withTopic(orderTopic)
            .withMessageCustomizer(msgBuilder -> msgBuilder.key(e.orderId()))
            .send();

    return event.orderId();

    // burada işini yap (stok düş, vb). Hata atarsan redelivery/dlq devreye girer.
  }
}
