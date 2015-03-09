// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.util.concurrent.*;
import org.data.*;



public class AsyncEventEmitter extends EventEmitter implements Runnable {
    
    // data
    String event;
    Map args;
    
    // static data
    static ExecutorService threads = Executors.newCachedThreadPool();
    
    
    @Override
    public AsyncEventEmitter emit(String event, Map args) {
        this.event = event;
        this.args = args;
        threads.submit(this);
        return this;
    }
    
    
    @Override
    public AsyncEventEmitter emit(String event, Object... args) {
        return emit(event, Coll.map(args));
    }
    
    
    @Override
    public void run() {
        super.emit(event, args);
    }
}
