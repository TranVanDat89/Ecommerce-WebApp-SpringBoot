package com.dattran.ecommerceapp.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/test")
public class TestSystemController {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @GetMapping("/cpu")
    public ResponseEntity<String> intensiveCpuLoad(@RequestParam(defaultValue = "30") int durationSeconds) {
        long startTime = System.currentTimeMillis();
        long duration = durationSeconds * 1000L;

        // Tạo multiple threads để sử dụng tất cả CPU cores
        int numThreads = Runtime.getRuntime().availableProcessors();
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                try {
                    long endTime = startTime + duration;
                    while (System.currentTimeMillis() < endTime) {
                        // CPU intensive operations
                        for (int j = 0; j < 1000000; j++) {
                            Math.sqrt(Math.random());
                            Math.sin(Math.random());
                            Math.cos(Math.random());
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(500).body("Load test interrupted");
        }

        return ResponseEntity.ok(String.format(
                "CPU Load Test completed: %d seconds, %d threads",
                durationSeconds, numThreads
        ));
    }

    @GetMapping("/memory")
    public ResponseEntity<String> intensiveMemoryLoad(@RequestParam(defaultValue = "30") int durationSeconds) {
        List<byte[]> memoryConsumers = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        long duration = durationSeconds * 1000L;

        try {
            while (System.currentTimeMillis() - startTime < duration) {
                // Allocate 10MB chunks
                byte[] chunk = new byte[10 * 1024 * 1024];
                Arrays.fill(chunk, (byte) 1); // Actually use the memory
                memoryConsumers.add(chunk);

                // Sleep briefly to control allocation rate
                Thread.sleep(100);
            }

            return ResponseEntity.ok(String.format(
                    "Memory Load Test completed: %d MB allocated",
                    memoryConsumers.size() * 10
            ));

        } catch (OutOfMemoryError e) {
            return ResponseEntity.ok("Memory Load Test: Out of memory reached");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(500).body("Memory test interrupted");
        } finally {
            memoryConsumers.clear(); // Allow GC
        }
    }

    @GetMapping("/combined")
    public ResponseEntity<String> combinedLoad(@RequestParam(defaultValue = "60") int durationSeconds) {
        CompletableFuture<String> cpuTask = CompletableFuture.supplyAsync(() -> {
            intensiveCpuLoad(durationSeconds);
            return "CPU load completed";
        });

        CompletableFuture<String> memoryTask = CompletableFuture.supplyAsync(() -> {
            intensiveMemoryLoad(durationSeconds);
            return "Memory load completed";
        });

        try {
            CompletableFuture.allOf(cpuTask, memoryTask).get();
            return ResponseEntity.ok("Combined load test completed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Combined load test failed: " + e.getMessage());
        }
    }

    @GetMapping("/load-status")
    public ResponseEntity<Map<String, Object>> getLoadStatus() {
        Runtime runtime = Runtime.getRuntime();

        Map<String, Object> status = new HashMap<>();
        status.put("availableProcessors", runtime.availableProcessors());
        status.put("totalMemoryMB", runtime.totalMemory() / (1024 * 1024));
        status.put("freeMemoryMB", runtime.freeMemory() / (1024 * 1024));
        status.put("usedMemoryMB", (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024));
        status.put("maxMemoryMB", runtime.maxMemory() / (1024 * 1024));

        return ResponseEntity.ok(status);
    }
}
