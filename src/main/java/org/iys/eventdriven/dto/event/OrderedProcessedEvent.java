package org.iys.eventdriven.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderedProcessedEvent(@NotNull String status,
                                    @NotNull String orderId,
                                    @NotNull String customerId) {}
