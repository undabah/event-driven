package org.iys.eventdriven.component;

import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.SubscriptionType;
import org.iys.eventdriven.dto.job.JobCommand;
import org.iys.eventdriven.dto.job.JobCompleted;
import org.iys.eventdriven.enums.WorkflowStep;
import org.iys.eventdriven.service.WorkflowStateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class WorkflowOrchestrator {

  private final PulsarTemplate<JobCommand> cmdTemplate;
  private final WorkflowStateService state;
  @Value("${app.pulsar.topic.job-completed}") private String jobCompletedTopic;

  public void start(String workflowId, String payloadJson) {
    if (state.tryStartWorkflow(workflowId)) {
      publishCommand(workflowId, WorkflowStep.JOB_A, payloadJson);
    }
  }

  @PulsarListener(
      topics = "${app.pulsar.topic.job-completed}",
      subscriptionName = "workflow-orchestrator",
      subscriptionType = SubscriptionType.Key_Shared // aynı workflow key ile sıralı olur
  )
  @TransactionalEventListener
  public void onJobCompleted(JobCompleted evt) {
    if (!state.acceptCompletionEvent(evt)) return; // idempotency guard

    var step = WorkflowStep.valueOf(evt.step());
    if (!evt.success()) {
      state.markFailed(evt.workflowId(), step, evt.message());
      return;
    }

    state.markSucceeded(evt.workflowId(), step);
    step.next().ifPresentOrElse(
        next -> publishCommand(evt.workflowId(), next, "{}"),
        () -> state.markFinished(evt.workflowId())
    );
  }

  private void publishCommand(String workflowId, WorkflowStep step, String payloadJson) {
    var cmd = new JobCommand(workflowId, step.name(), payloadJson);
    cmdTemplate.newMessage(cmd)
        .withTopic(topicForStep(step))
        .withMessageCustomizer(mb -> mb.key(workflowId)) // ordering key
        .send();
  }

  private String topicForStep(WorkflowStep step) {
    return switch (step) {
      case JOB_A -> "persistent://public/default/job-command-JOB_A";
      case JOB_B -> "persistent://public/default/job-command-JOB_B";
      case JOB_C -> "persistent://public/default/job-command-JOB_C";
    };
  }
}
