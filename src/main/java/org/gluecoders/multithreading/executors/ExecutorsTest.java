package org.gluecoders.multithreading.executors;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExecutorsTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        TimeTakingService remoteService = new TimeTakingService();
        List<Callable<Integer>> callables = IntStream.range(0,20)
                .mapToObj(i -> (Callable<Integer>)()->remoteService.get(()->i))
                .collect(Collectors.toList());
        System.out.println(Instant.now() + " Calling timetakingservice");
        List<Future<Integer>> results = executorService.invokeAll(callables);
        for(Future<Integer> result : results){
            while(!result.isDone());
            System.out.println("Result " +result.get());
        }
        System.out.println(Instant.now() + " done");
        executorService.shutdownNow();
    }
}
