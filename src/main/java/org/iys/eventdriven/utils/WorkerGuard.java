package org.iys.eventdriven.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class WorkerGuard {

  private final StringRedisTemplate redis;
  private static final Duration TTL = Duration.ofMinutes(10);

  public boolean acquire(String workflowId, String step) {
    String key = "guard:" + workflowId + ":" + step;
    Boolean ok = redis.opsForValue().setIfAbsent(key, "1", TTL);
    return Boolean.TRUE.equals(ok);
  }
  public void release(String workflowId, String step) {
    redis.delete("guard:" + workflowId + ":" + step);
  }
}
