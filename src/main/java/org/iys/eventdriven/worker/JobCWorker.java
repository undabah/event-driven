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
public class JobCWorker {

  private final PulsarTemplate<JobCompleted> completedTemplate;
  private final WorkerGuard guard;
  @Value("${app.pulsar.topic.job-completed}") private String completedTopic;

  @PulsarListener(
      topics = "${app.pulsar.topic.job-completed}",
      subscriptionName = "job-c-on-b-completed",
      subscriptionType = SubscriptionType.Key_Shared
  )
  public void onUpstreamCompleted(JobCompleted evt) {
    if (!evt.success() || !"JOB_B".equals(evt.step())) return;
    if (!guard.acquire(evt.workflowId(), "JOB_C")) return;

    boolean success = false; String msg = "ok"; String labeled = "";
    try {
      String txt = evt.outputJson();
      labeled = "{ \"text\": \"" + txt + "\", \"label\": \"cleaned\" }";
      success = true;
    } catch (Exception e) { msg = e.getMessage(); labeled = "{}"; }
    finally {
      var done = new JobCompleted(evt.workflowId(), "JOB_C", success, msg, labeled);
      completedTemplate.newMessage(done)
          .withTopic(completedTopic)
          .withMessageCustomizer(mb -> mb.key(evt.workflowId()))
          .send();
      guard.release(evt.workflowId(), "JOB_C");
    }
  }
}
