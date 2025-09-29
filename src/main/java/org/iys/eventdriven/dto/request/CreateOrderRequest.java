package org.iys.eventdriven.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(@NotNull String customerId, @NotNull String sku, @NotNull Integer quantity) {}
