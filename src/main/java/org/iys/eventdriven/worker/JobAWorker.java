package org.iys.eventdriven.worker;

import lombok.RequiredArgsConstructor;
import org.iys.eventdriven.dto.job.JobCompleted;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobAWorker {
  private final PulsarTemplate<JobCompleted> completedTemplate;
  @Value("${app.pulsar.topic.job-completed}") private String completedTopic;

  //WorkerA startEvent ile tetiklenir.
  public void runA(String workflowId, String rawHtml) {
    boolean success = true; String msg = "ok";
    String cleaned = rawHtml.replaceAll("<[^>]*>", "");
    var evt = new JobCompleted(workflowId, "JOB_A", success, msg, cleaned);
    completedTemplate.newMessage(evt)
        .withTopic(completedTopic)
        .withMessageCustomizer(mb -> mb.key(workflowId))
        .send();
  }
}
