package org.iys.eventdriven.dto.job;

public record JobCommand(String workflowId, String step, String payloadJson) {}
