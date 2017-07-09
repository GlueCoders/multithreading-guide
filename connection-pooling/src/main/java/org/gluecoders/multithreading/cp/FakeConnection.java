package org.gluecoders.multithreading.cp;

/**
 * Created by Anand_Rajneesh on 6/18/2017.
 */
public final class FakeConnection {

    /**
     * Random looping
     */
    public void work() {
        long random = (int) (Math.random()*10000000);
        for(long i = 0; i <=random;i++){
            System.out.print("");
        }
    }
}
