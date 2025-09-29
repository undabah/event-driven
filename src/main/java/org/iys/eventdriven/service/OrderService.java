package org.iys.eventdriven.service;

import lombok.RequiredArgsConstructor;
import org.iys.eventdriven.dto.CreateOrderRequest;
import org.iys.eventdriven.dto.OrderCreatedEvent;
import org.iys.eventdriven.entity.OrderEntity;
import org.iys.eventdriven.repository.OrderRepository;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;


@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository repo;
  private final PulsarTemplate<OrderCreatedEvent> pulsarTemplate;

  @Value("${app.pulsar.topic.order-events}")
  private String orderTopic;

  @Transactional
  public String createOrder(CreateOrderRequest req) {
    OrderEntity entity = new OrderEntity();
    entity.setId(UUID.randomUUID().toString());
    entity.setCustomerId(req.customerId());
    entity.setSku(req.sku());
    entity.setQuantity(req.quantity());
    entity.setStatus("CREATED");
    entity.setCreatedAt(Instant.now());
    repo.save(entity);

    OrderCreatedEvent event = new OrderCreatedEvent(
            "v1", UUID.randomUUID(), Instant.now(),
            entity.getId(), entity.getCustomerId(), entity.getQuantity(), entity.getSku()
    );

    pulsarTemplate.newMessage(event)
            .withTopic(orderTopic)
            .withMessageCustomizer(msgBuilder -> msgBuilder.key(entity.getId())) // key-based routing
            .send();

    return entity.getId();
  }
}
