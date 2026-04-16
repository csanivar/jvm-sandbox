package org.example;

import java.util.HashMap;
import java.util.Map;

public class ReadWriteLockDemo {

    static class CustomReadWriteLock {
        private int readers = 0;
        private int writers = 0;
        private int writeRequests = 0;

        public synchronized void lockRead() throws InterruptedException {
            while (writers > 0 || writeRequests > 0) {
                wait();
            }
            readers++;
        }

        public synchronized void unlockRead() {
            readers--;
            if (readers == 0) {
                notifyAll();
            }
        }

        public synchronized void lockWrite() throws InterruptedException {
            writeRequests++;
            while (readers > 0 || writers > 0) {
                wait();
            }
            writeRequests--;
            writers++;
        }

        public synchronized void unlockWrite() {
            writers--;
            notifyAll();
        }
    }

    static class ThreadSafeCache {
        private final Map<String, String> cache = new HashMap<>();
        private final CustomReadWriteLock rwLock = new CustomReadWriteLock();
        public void put(String key, String value) {
            try {
                rwLock.lockWrite();
                log("ACQUIRED write lock", "Writing key=" + key);

                Thread.sleep(1000);
                cache.put(key, value);
                log("FINISHED writing", "key=" + key);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                rwLock.unlockWrite();
            }
        }

        public void get(String key) {
            try {
                rwLock.lockRead();
                log("ACQUIRED read lock", "Reading key=" + key);

                Thread.sleep(1000);
                String val = cache.getOrDefault(key, "DEFAULT");

                log("FINISHED reading", "value=" + val);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                rwLock.unlockRead();
            }
        }

        private static synchronized void log(String action, String detail) {
            System.out.printf("[%-10s] %-22s | %s%n",
                    Thread.currentThread().getName(), action, detail);
        }
    }

    public void run(String[] args) {
        ThreadSafeCache sharedCache = new ThreadSafeCache();

        Runnable readTask = () -> sharedCache.get("document_1");
        Runnable writeTask = () -> sharedCache.put("document_1", "MongDB Data 1");

        new Thread(readTask, "Reader-1").start();
        new Thread(readTask, "Reader-2").start();
        new Thread(readTask, "Reader-3").start();

        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        new Thread(writeTask, "Writer-1").start();

        new Thread(readTask, "Reader-4").start();
        new Thread(readTask, "Reader-5").start();
    }
}
