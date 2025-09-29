package org.iys.eventdriven.dto.event;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
        String schemaVersion,
        @NotNull UUID eventId,
        @NotNull Instant occurredAt,
        @NotNull String orderId,
        @NotNull String customerId,
        @NotNull Integer quantity,
        @NotNull String sku,
        @NotNull String status
) {}
