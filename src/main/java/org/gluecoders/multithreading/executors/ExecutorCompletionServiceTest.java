package org.gluecoders.multithreading.executors;

import java.time.Instant;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ExecutorCompletionServiceTest {


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ExecutorCompletionService<Integer> executorCompletionService = new ExecutorCompletionService<>(executorService);
        TimeTakingService remoteService = new TimeTakingService();
        System.out.println(Instant.now() + " Calling timetakingservice");
        IntStream.range(0,20)
                .mapToObj(i -> (Callable<Integer>)()->remoteService.get(()->i))
                .forEach(executorCompletionService::submit);

        for(int i = 0; i<20;i++){
            System.out.println("Result " + executorCompletionService.take().get());
        }
        System.out.println(Instant.now() + " done");
        executorService.shutdownNow();
    }
}
