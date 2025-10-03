package org.iys.eventdriven.dto.job;

public record JobCompleted(
    String workflowId,
    String step,
    boolean success,
    String message,
    String outputJson
) {}
