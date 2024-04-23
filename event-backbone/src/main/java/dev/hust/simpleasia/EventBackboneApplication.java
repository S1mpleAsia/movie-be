
package dev.hust.simpleasia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EventBackboneApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventBackboneApplication.class, args);
    }
}
