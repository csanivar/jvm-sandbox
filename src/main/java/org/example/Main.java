package org.example;

public class Main {
    public static void main(String[] args) {

        System.out.println("jvm-sandbox ready — Java " + Runtime.version());
//        runReadWriteLockDemo(args);
//        runReentrantReadWriteLockDemo(args);
        runStampedLockDemo(args);
    }

    public static void runReadWriteLockDemo(String[] args) {
        ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();
        readWriteLockDemo.run(args);
    }

    public static void runReentrantReadWriteLockDemo(String[] args) {
        ReentrantReadWriteLockDemo demo = new ReentrantReadWriteLockDemo();
        demo.run(args);
    }

    public static void runStampedLockDemo(String[] args) {
        StampedLockDemo demo = new StampedLockDemo();
        try {
            demo.run(args);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
