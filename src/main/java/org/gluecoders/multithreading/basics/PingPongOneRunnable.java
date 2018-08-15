package org.gluecoders.multithreading.basics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PingPongOneRunnable {

    private final static class Play implements Runnable {

        private final String shot;
        private final Consumer<AtomicInteger> counterUpdater;
        private final Function<AtomicInteger, Boolean> whilePredicate;
        private final Supplier<Boolean> shouldStart;
        private final Runnable started;
        private final Object lock;
        private final AtomicInteger counter;

        private Play(String shot, Consumer<AtomicInteger> counterUpdater, Function<AtomicInteger, Boolean> whilePredicate, Supplier<Boolean> shouldStart, Runnable started, Object lock, AtomicInteger counter) {
            this.shot = shot;
            this.counterUpdater = counterUpdater;
            this.whilePredicate = whilePredicate;
            this.shouldStart = shouldStart;
            this.started = started;
            this.lock = lock;
            this.counter = counter;
        }

        @Override
        public void run() {
            if (shouldStart.get()) {
                synchronized (lock) {
                    started.run();
                    while (whilePredicate.apply(counter)) {
                        System.out.println(shot + " " + counter.get());
                        counterUpdater.accept(counter);
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
    }


    public static void main(String[] args) {
        try {
            AtomicInteger counter = new AtomicInteger(0);
            CountDownLatch startLatch = new CountDownLatch(1);
            Object lock = new Object();
            Play ping = new Play("Ping", (arg) -> {
            }, (arg) -> arg.get() < 10,
                    () -> true, startLatch::countDown, lock, counter);
            Play pong = new Play("Pong", AtomicInteger::incrementAndGet, (arg) -> arg.get() < 10,
                    () -> {
                        try {
                            startLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }, () -> {
            }, lock, counter);

            Thread t2 = new Thread(ping);
            Thread t1 = new Thread(pong);
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
