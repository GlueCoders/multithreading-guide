package org.gluecoders.multithreading.cp;

import java.util.stream.IntStream;

/**
 * Created by Anand_Rajneesh on 6/18/2017.
 */
public class Driver {

    public static void main(String[] args) {
        try {
            Thread.sleep(20000);
            int max = (int) (Math.random()*100);
            //max = no of connections
            final Pool<FakeConnection> pool = Pool.of(FakeConnection::new, max);
            //threadCount = no of threads running simultaneously for acquiring connection
            int threadCount = 2 * max;
            System.out.println("Creating threads "+ threadCount);
            //Create Runnable instances and start them in threads
            IntStream.rangeClosed(1, threadCount)
                    .mapToObj(i -> (Runnable) () -> {
                        long time = System.currentTimeMillis();
                        try {
                            FakeConnection conn = pool.acquire();
                            System.out.printf("Connection acquired by %s after %s secs %n", Thread.currentThread().getName(), (System.currentTimeMillis() - time)/1000);
                            time = System.currentTimeMillis();
                            conn.work();
                            System.out.printf("Work done by %s time taken %s secs %n", Thread.currentThread().getName(), (System.currentTimeMillis() - time)/1000);
                            pool.release(conn);
                        } catch (ResourceException e) {
                            e.printStackTrace();
                        }
                    })
                    .map(Thread::new)
                    .forEach(Thread::start);
            //Customary sleep just for Thread Analysers
            Thread.sleep(100000L);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
