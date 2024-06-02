package com.dattran.ecommerceapp.components;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomHealthCheck implements HealthIndicator {
    @Override
    public Health health() {
        try {
            Map<String, Object> details = new HashMap<>();
            //DOWN => 503
            //Healthcheck for Kafka
//            String clusterId = kafkaAdmin.clusterId();
//            if (clusterId.isEmpty()) {
//                return Health.down()
//                        .withDetail("Error", "Cannot get cluster's id").build();
//            } else {
//                details.put("kafka", String.format("Cluster's id: %s", clusterId));
//            }
            String computerName = InetAddress.getLocalHost().getHostName();
            details.put("computerName", String.format("computerName: %s", computerName));
            return Health.up().withDetails(details).build();
            //return Health.up().withDetail("computerName", computerName).build();//code: 200
        } catch (Exception e) {
            return Health.down()
                    .withDetail("Error", e.getMessage()).build();
        }
    }
}
