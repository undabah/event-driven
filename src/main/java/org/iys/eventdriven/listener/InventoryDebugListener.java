// package: org.iys.eventdriven.listener
package org.iys.eventdriven.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.SubscriptionType;
import org.iys.eventdriven.config.PulsarTopicProps;
import org.iys.eventdriven.dto.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryDebugListener {

   private final PulsarTopicProps topics;

  @PulsarListener(
      topics = "#{@pulsarTopicProps.orderEvents}",
      subscriptionName = "inventory-svc",
      subscriptionType = SubscriptionType.Key_Shared
  )
  public void onOrder(OrderCreatedEvent e) {
    log.info("📥 Order received -> id={} sku={} qty={} customer={}",
        e.orderId(), e.sku(), e.quantity(), e.customerId());

    // burada işini yap (stok düş, vb). Hata atarsan redelivery/dlq devreye girer.
  }
}
