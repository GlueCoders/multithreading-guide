package org.gluecoders.multithreading.executors;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Just to simulate a method call which takes random time to complete, like an HTTP call or IO operation
 */
public class TimeTakingService {

    private final static Random random = new Random();

    public <T> T get(Supplier<T> supplier){
        try {
            int sleepTime = 5000;
            System.out.println("Sleeping for time: "+ sleepTime + " on thread : "+ Thread.currentThread().getName());
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return supplier.get();
    }

}
