// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.util.concurrent.*;



public class Eventer implements Runnable {
    
    // static data
    static ExecutorService threads = Executors.newCachedThreadPool();
    
    // data
    Map args;
    String event;
    
    public void emit(String event, Map args) {
        this.event = event;
        this.args = args;
        threads.submit(this);
    }
    
    public void absorb(String event, Map args) {
        
    }
    
    @Override
    public void run() {
        absorb(event, args);
    }
}
