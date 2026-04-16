package org.example;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    private static final Semaphore pool = new Semaphore(2);

    public void run() {
        Runnable dbQueryTask = () -> {
            String threadName = Thread.currentThread().getName();
            try {
                // log thread is waiting for connection
                System.out.println("⏳ " + threadName + " is waiting for a database connection...");

                pool.acquire();
                System.out.println("  ✅ " + threadName + " ACQUIRED a connection! Executing query...");

                // log that we acuqired it
                Thread.sleep(1000); // simulate task
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                // log that it's finished
                System.out.println("  ⬅️ " + threadName + " RELEASED the connection.");

                pool.release();
            }
        };

        for (int i=0; i<5; i++) {
            new Thread(dbQueryTask, "Thread-" + i).start();
        }
    }
}
