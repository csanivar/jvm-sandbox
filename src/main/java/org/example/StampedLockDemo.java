package org.example;

import java.util.concurrent.locks.StampedLock;

public class StampedLockDemo {

    static class ThreadSafePoint {
        private double x, y;
        private final StampedLock sl = new StampedLock();

        public ThreadSafePoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void move(double dx, double dy) {
            long stamp = sl.writeLock();
            try {
                System.out.println("  ✏️ Writer acquired exclusive write lock. Moving point...");
                x += dx;

                try { Thread.sleep(200); } catch (InterruptedException ignored) {}

                y += dy;
                System.out.println("  ✅ Writer finished moving to (" + x + ", " + y + "). Releasing lock.");
            } finally {
                sl.unlockWrite(stamp);
            }
        }

        public void fastRead() {
            long stamp = sl.tryOptimisticRead();
            double cx = x;
            double cy = y;

            if (!sl.validate(stamp)) {
                System.out.println("  ⚠️ [Fast] Optimistic read failed. Falling back.");
                stamp = sl.readLock();
                try {
                    cx = x;
                    cy = y;
                } finally {
                    sl.unlockRead(stamp);
                }
            } else {
                System.out.println("  ⚡ [Fast] Optimistic read SUCCESSFUL. Zero locks acquired!");
            }
            System.out.println("  ✔️ [Fast] Distance calculated: " + Math.sqrt(cx * cx + cy * cy));
        }

        public void slowRead() {
            long stamp = sl.tryOptimisticRead();
            System.out.println("  📖 [Slow] Reader got optimistic stamp: " + stamp);
            double cx = x;

            System.out.println("  ⏱️ [Slow] Reader paused... giving writer a chance to jump in.");
            try {Thread.sleep(1000); } catch (InterruptedException ignored) {}
            double cy = y;

            if (!sl.validate(stamp)) {
                System.out.println("  ⚠️ [Slow] Stamp " + stamp + " is INVALID! Writer changed data.");
                System.out.println("  🔒 [Slow] Falling back to pessimistic lock...");
                stamp = sl.readLock(); // Block and wait for writer to finish
                try {
                    cx = x;
                    cy = y;
                    System.out.println("  🔒 [Slow] Re-read safely under lock.");
                } finally {
                    sl.unlockRead(stamp);
                }
            } else {
                System.out.println("  ⚡ [Slow] Optimistic read SUCCESSFUL.");
            }

            System.out.println("  ✔️ [Slow] Final calculated distance using (" + cx + ", " + cy + "): " + Math.sqrt(cx * cx + cy * cy));
        }
    }

    public void run(String[] args) throws InterruptedException {
        ThreadSafePoint point = new ThreadSafePoint(3.0, 4.0);
        System.out.println("--- TEST 1: The Happy Path (No Writers Active) ---");
        point.fastRead();

        System.out.println("\n--- TEST 2: The Conflict Path (Writer Interrupts Reader) ---");
        Thread readerThread = new Thread(() -> point.slowRead());

        Thread writerThread = new Thread(() -> point.move(2.0, 8.0));

        readerThread.start();
        Thread.sleep(100);

        writerThread.start();

        readerThread.join();
        writerThread.join();
    }
}
