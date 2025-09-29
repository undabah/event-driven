package org.iys.eventdriven.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.iys.eventdriven.dto.request.CreateOrderRequest;
import org.iys.eventdriven.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService service;

  @PostMapping("/create")
  public ResponseEntity<?> create(@RequestBody @Valid CreateOrderRequest req) {
    String orderId = service.createOrder(req);
    return ResponseEntity.accepted().body(Map.of("orderId", orderId));
  }
}
