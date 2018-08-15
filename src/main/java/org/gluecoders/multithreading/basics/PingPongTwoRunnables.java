package org.gluecoders.multithreading.basics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class PingPongTwoRunnables {

    private static final class Ping implements Runnable {

        private final AtomicInteger counter;
        private final Object lock;
        private final CountDownLatch startLatch;

        private Ping(AtomicInteger counter, Object lock, CountDownLatch startLatch) {
            this.counter = counter;
            this.lock = lock;
            this.startLatch = startLatch;
        }


        @Override
        public void run() {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock) {
                while (counter.get() < 10) {
                    System.out.println("ping" + counter.get());
                    if (counter.get() == 0) {
                        startLatch.countDown();
                    }
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.notify();
            }
        }
    }

    private static final class Pong implements Runnable {
        private final AtomicInteger counter;
        private final Object lock;
        private final CountDownLatch startLatch;

        private Pong(AtomicInteger counter, Object lock, CountDownLatch startLatch) {
            this.counter = counter;
            this.lock = lock;
            this.startLatch = startLatch;
        }

        @Override
        public void run() {
            try {
                startLatch.await();
                while (counter.get() < 10) {
                    synchronized (lock) {
                        System.out.println("pong " + counter.get());
                        counter.incrementAndGet();
                        lock.notify();
                        lock.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            Object lock = new Object();
            CountDownLatch startLatch = new CountDownLatch(1);
            AtomicInteger counter = new AtomicInteger(0);
            Thread t1 = new Thread(new Ping(counter, lock, startLatch));
            Thread t2 = new Thread(new Pong(counter, lock, startLatch));
            t1.start();
            t2.start();
            //t1.join();
            //t2.join();
            System.out.println("leaving main");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

