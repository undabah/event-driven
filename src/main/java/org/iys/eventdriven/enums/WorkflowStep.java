package org.iys.eventdriven.enums;

import java.util.Optional;

public enum WorkflowStep {
  JOB_A, JOB_B, JOB_C;
  public Optional<WorkflowStep> next() {
    return switch (this) {
      case JOB_A -> Optional.of(JOB_B);
      case JOB_B -> Optional.of(JOB_C);
      case JOB_C -> Optional.empty();
    };
  }
}