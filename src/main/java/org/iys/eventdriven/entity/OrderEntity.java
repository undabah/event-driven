package org.iys.eventdriven.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {
  @Id
  String id;
  String customerId;
  String sku;
  Integer quantity;
  String status;
  Instant createdAt;
}
