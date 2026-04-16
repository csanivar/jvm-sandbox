package org.example;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    private static final CountDownLatch latch = new CountDownLatch(3);

    public void run() throws InterruptedException {
        Runnable task = () -> {
            String shardName = Thread.currentThread().getName();

            try {
                System.out.println("🔍 " + shardName + " searching for data...");

                Thread.sleep((long) (Math.random() * 1000));
                System.out.println("  ✅ " + shardName + " found the data!");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        };

        new Thread(task, "Shard-1").start();
        new Thread(task, "Shard-2").start();
        new Thread(task, "Shard-3").start();

        latch.await();

        System.out.println("🎉 Coordinator Node: All shards replied! Merging data and returning to user.");
    }
}
