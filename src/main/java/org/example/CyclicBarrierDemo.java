package org.example;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    private static final CyclicBarrier barrier = new CyclicBarrier(3, () ->
            System.out.println("\n🏁 ALL THREADS ARRIVED! Barrier opened. Moving to next phase...\n"));

    public void run() {
        Runnable worker = () -> {
            String workerName = Thread.currentThread().getName();

            try {
                System.out.println("🛠️ " + workerName + " executing Phase 1 (Map)...");
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println("  🛑 " + workerName + " finished Phase 1. Waiting at barrier.");
                barrier.await();

                System.out.println("⚙️ " + workerName + " executing Phase 2 (Reduce)...");
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println("  🛑 " + workerName + " finished Phase 2. Waiting at barrier.");
                barrier.await();

            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        };

        new Thread(worker, "Worker-1").start();
        new Thread(worker, "Worker-2").start();
        new Thread(worker, "Worker-3").start();
    }
}
