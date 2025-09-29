package org.iys.eventdriven;

import org.iys.eventdriven.config.PulsarTopicProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class EventdrivenApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventdrivenApplication.class, args);
    }

}
