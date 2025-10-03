package org.iys.eventdriven.service;

import org.iys.eventdriven.dto.job.JobCompleted;
import org.iys.eventdriven.enums.WorkflowStep;
import org.springframework.stereotype.Service;

@Service
public class WorkflowStateService {
  public boolean tryStartWorkflow(String workflowId) { /* unique start */ return true; }
  public boolean acceptCompletionEvent(JobCompleted e) { /* idem */ return true; }
  public void markSucceeded(String wf, WorkflowStep step) { /* update */ }
  public void markFailed(String wf, WorkflowStep step, String msg) { /* update */ }
  public void markFinished(String wf) { /* update */ }
}
