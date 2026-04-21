package org.example;

import org.example.problems.SemanticVersionCompare;

public class Main {
    public static void main(String[] args) {

        System.out.println("jvm-sandbox ready — Java " + Runtime.version());
//        runReadWriteLockDemo(args);
//        runReentrantReadWriteLockDemo(args);
//        runStampedLockDemo(args);
//        runSemaphoreDemo();
//        runLongAdderDemo();
//        runCountDownLatchDemo();
//        runCyclicBarrierDemo();
        runSemanticVersionCompare();
    }

    public static void runSemanticVersionCompare() {
        SemanticVersionCompare cls = new SemanticVersionCompare();
        cls.test();
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

    public static void runSemaphoreDemo() {
        new SemaphoreDemo().run();
    }

    public static void runLongAdderDemo() {
        try {
            new LongAdderDemo().run();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void runCountDownLatchDemo() {
        try {
            new CountDownLatchDemo().run();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void runCyclicBarrierDemo() {
        new CyclicBarrierDemo().run();
    }
}
