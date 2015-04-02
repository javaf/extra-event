// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.util.concurrent.*;



/**
 * <b>Slow Reflexive to a stimulus</b>
 * <div>Encapsulates a slow reaction that is executed asynchronously</div>
 * @author wolfram77
 */
public class Reaction implements Reflexive {
    
    // data
    Reflexive reaction;
    
    // static data
    static final ExecutorService threads = Executors.newCachedThreadPool((Runnable r) -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });
    
    // init code
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            threads.shutdown();
            try { threads.awaitTermination(3650, TimeUnit.DAYS); }
            catch(InterruptedException e) {}
        }));
    }
    
    
    /**
     * <b>Create a Slow Reflexive</b>
     * @param reaction slow reaction
     */
    public Reaction(Reflexive reaction) {
        this.reaction = reaction;
    }
    
    
    /**
     * <b>Invoke the slow reaction asynchronously</b>
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    @Override
    public void on(String stimulus, Map args) {
        threads.submit(() -> reaction.on(stimulus, args));
    }
}
