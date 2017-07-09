package org.gluecoders.multithreading.cp;

/**
 * Created by Anand_Rajneesh on 6/18/2017.
 */
public class ResourceException extends Exception {

    public ResourceException(String s, InterruptedException e) {
        super(s,e);
    }
}
