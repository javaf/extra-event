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

    
    // Emit (event, args)
    // - emit an event asynchronously
    @Override
    public AsyncEventEmitter emit(String event, Map args) {
        this.event = event;
        this.args = args;
        threads.submit(this);
        return this;
    }
    
    
    // Emit (event, args...)
    // - emit an event asynchronously
    @Override
    public AsyncEventEmitter emit(String event, Object... args) {
        return emit(event, Coll.map(args));
    }
    
    
    // Run ()
    // - does the task of emitting event
    @Override
    public void run() {
        super.emit(event, args);
    }
}
