package org.iys.eventdriven.service;

import lombok.AllArgsConstructor;
import org.iys.eventdriven.dto.job.JobCompleted;
import org.iys.eventdriven.enums.JobStatus;
import org.iys.eventdriven.enums.WorkflowStep;
import org.iys.eventdriven.repository.JobExecutionRepo;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WorkflowStateService {

  private final JobExecutionRepo jobExecutionRepo;

  public boolean tryStartWorkflow(String workflowId) { /* unique start */ return true; }
  public boolean acceptCompletionEvent(JobCompleted e) {
    if (jobExecutionRepo.existsByWorkflowIdAndStepAndStatus(e.workflowId(), e.step(), JobStatus.SUCCESS)) {
      return false; // bu event daha önce işlenmiş
    }
    if (jobExecutionRepo.existsByWorkflowIdAndStepAndStatus(e.workflowId(), e.step(), JobStatus.FAILED)) {
      return false;
    }

    return true;
  }
  public void markSucceeded(String wf, WorkflowStep step) { /* update */ }
  public void markFailed(String wf, WorkflowStep step, String msg) { /* update */ }
  public void markFinished(String wf) { /* update */ }
}
