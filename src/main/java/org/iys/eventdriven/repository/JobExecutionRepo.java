package org.iys.eventdriven.repository;

import org.iys.eventdriven.entity.JobExecution;
import org.iys.eventdriven.enums.JobStatus;
import org.iys.eventdriven.enums.WorkflowStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobExecutionRepo extends JpaRepository<JobExecution, UUID> {
  Optional<JobExecution> findByWorkflowIdAndStep(String workflowId, WorkflowStep step);
  boolean existsByWorkflowIdAndStepAndStatus(String workflowId, String WorkFlowStep, JobStatus step);
}
