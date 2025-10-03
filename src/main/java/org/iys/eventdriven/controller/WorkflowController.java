package org.iys.eventdriven.controller;

import lombok.RequiredArgsConstructor;
import org.iys.eventdriven.component.WorkflowOrchestrator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workflow")
public class WorkflowController {
  private final WorkflowOrchestrator orchestrator;

  @PostMapping("/start/{workflowId}")
  public String start(@PathVariable String workflowId, @RequestBody(required=false) String payload) {
    orchestrator.start(workflowId, payload == null ? "{}" : payload);
    return "OK";
  }
}
