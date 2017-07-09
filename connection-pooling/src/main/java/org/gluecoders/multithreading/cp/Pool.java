package org.gluecoders.multithreading.cp;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Created by Anand_Rajneesh on 6/18/2017.
 */
public class Pool<T> {

    private Semaphore access;
    private ConcurrentLinkedQueue<T> resources;

    /**
     * Initializes semaphore and resources with Supplier.get() provided
     * @param supplier - Suppiler for connection or whatever pool needs to maintain
     * @param maxAllowed - max instances allowed at a time
     */
    private Pool(Supplier<T> supplier, int maxAllowed) {
        System.out.println(String.format("Initiating Pool of %s with maxAllowed %s", supplier, maxAllowed));
        access = new Semaphore(maxAllowed);
        resources = IntStream.rangeClosed(1, maxAllowed)
                .mapToObj(i -> supplier.get())
                .collect(ConcurrentLinkedQueue::new, ConcurrentLinkedQueue::add, ConcurrentLinkedQueue::addAll);
    }

    public static <T> Pool<T> of(Supplier<T> supplier, int maxAllowed) {
        return new Pool<>(supplier, maxAllowed);
    }

    /**
     * Acquire resource from the pool. Blocks if no resource is available.
     * @return T resource will never be null
     * @throws ResourceException
     */
    public T acquire() throws ResourceException {
        try {
            System.out.println("Trying to acquire resource for "+Thread.currentThread().getName());
            //Blocking if no permit is available, blocks until some thread has called release()
            access.acquire();
            return resources.poll();
        } catch (InterruptedException e) {
            throw new ResourceException("Could not acquire resource from Semaphore", e);
        }
    }

    /**
     * Release the resource back to pool, permit is added back to semaphore
     * @param resource
     */
    public void release(T resource) {
        System.out.println("Releasing resource from "+Thread.currentThread().getName());
        resources.offer(resource);
        access.release();
    }

}
