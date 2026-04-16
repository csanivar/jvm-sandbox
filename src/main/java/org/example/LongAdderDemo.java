package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class LongAdderDemo {

    private static final LongAdder total = new LongAdder();

    public void run() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        Runnable querySimulation = () -> {
            for(int i=0; i<10000; i++) {
                total.increment();
            }
        };

        for(int i=0; i<10; i++) {
            executor.submit(querySimulation);
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Total Queries Executed: " + total.sum()); // Exactly 100,000

    }
}
