// package: org.iys.eventdriven.config
package org.iys.eventdriven.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.pulsar.topic")
public class PulsarTopicProps {
  private String orderEvents;
  private String orderEventsDlt;

  public String getOrderEvents() { return orderEvents; }
  public void setOrderEvents(String v) { this.orderEvents = v; }
  public String getOrderEventsDlt() { return orderEventsDlt; }
  public void setOrderEventsDlt(String v) { this.orderEventsDlt = v; }
}
