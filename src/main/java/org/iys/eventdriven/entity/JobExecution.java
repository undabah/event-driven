package org.iys.eventdriven.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;
import org.iys.eventdriven.enums.JobStatus;
import org.iys.eventdriven.enums.WorkflowStep;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="job_execution",
       uniqueConstraints=@UniqueConstraint(columnNames={"workflow_id","step"}))
@DynamicUpdate
public class JobExecution {
  @Id private UUID id;
  @Column(name="workflow_id", nullable=false) private String workflowId;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private WorkflowStep step;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private JobStatus status;
  @Lob private String outputJson;
  @Lob private String errorText;
  private Instant startedAt;
  private Instant finishedAt;
  @Version private Integer version;
}
