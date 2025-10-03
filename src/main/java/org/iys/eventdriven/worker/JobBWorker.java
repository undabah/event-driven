package org.iys.eventdriven.worker;

import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.SubscriptionType;
import org.iys.eventdriven.dto.job.JobCompleted;
import org.iys.eventdriven.utils.WorkerGuard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobBWorker {

  private final PulsarTemplate<JobCompleted> completedTemplate;
  private final WorkerGuard guard;
  @Value("${app.pulsar.topic.job-completed}") private String completedTopic;

  @PulsarListener(
      topics = "${app.pulsar.topic.job-completed}",
      subscriptionName = "job-b-on-a-completed",
      subscriptionType = SubscriptionType.Key_Shared // aynı workflowId sıralı
  )
  public void onUpstreamCompleted(JobCompleted evt) {
    if (!evt.success() || !"JOB_A".equals(evt.step())) return;

    if (!guard.acquire(evt.workflowId(), "JOB_B")) return;

    boolean success = false; String msg = "ok"; String fixed = evt.outputJson();
    try {
      fixed = fixed.replace("�", "");
      success = true;
    } catch (Exception e) { msg = e.getMessage(); }
    finally {
      var done = new JobCompleted(evt.workflowId(), "JOB_B", success, msg, fixed);
      completedTemplate.newMessage(done)
          .withTopic(completedTopic)
          .withMessageCustomizer(mb -> mb.key(evt.workflowId()))
          .send();
      guard.release(evt.workflowId(), "JOB_B");
    }
  }
}
