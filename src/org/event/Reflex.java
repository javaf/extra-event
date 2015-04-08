// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.lang.reflect.*;
import java.util.concurrent.*;



/**
 * Response to a stimulus. <p>
 * Encapsulates a fast or <i>slow</i> response. Slow
 * response is run on a daemon thread. Any executing
 * slow response is aborted on application exit. </p>
 * @author wolfram77
 */
public class Reflex implements Reflexive {
    
    // data
    final Reflexive reflex;
    final Method mthd;
    final Object obj;
    boolean slow;
    
    // static data
    static final ExecutorService threads = Executors.newCachedThreadPool(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    });
    
    // init code
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
            @Override
            public void run() {
            threads.shutdown();
            try { threads.awaitTermination(3650, TimeUnit.DAYS); }
            catch(InterruptedException e) {}
        }}));
    }

    
    /**
     * Create reflex from method.
     * @param cls class of method
     * @param mthd name of method
     * @param bestatic should method be static?
     * @param gethandle is method handle required?
     */
    private Method _new(Class<?> cls, String mthd, boolean bestatic) {
        try {
            Method m = cls.getMethod(mthd, String.class, Map.class);
            boolean isstatic = Modifier.isStatic(m.getModifiers());
            if(isstatic != bestatic) throw new NoSuchMethodException("Method ["+m.getName()+"] is inaccessible");
            if(m.isAnnotationPresent(Speed.class)) speed(m.getAnnotation(Speed.class).value());
            return m;
        }
        catch(NoSuchMethodException e) { throw new RuntimeException(e); }
    }


    /**
     * Invoke reflex synchronously.
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void _on(String stimulus, Map args) {
        try {
            if(mthd == null) reflex.on(stimulus, args);
            else mthd.invoke(obj, stimulus, args);
        }
        catch(Throwable e) { throw new RuntimeException(e); }
    }
    
    
    /**
     * Encapsulate a reflex. <p>
     * Slow reflex must be encapsulated. </p>
     * @param reflex reflex to encapsulate
     */
    public Reflex(Reflexive reflex) {
        obj = null;
        mthd = null;
        this.reflex = reflex;
        _new(reflex.getClass(), "on", false);
    }
    
    
    /**
     * Create reflex from static method.
     * @param cls class containing the method
     * @param mthd name of the method
     */
    public Reflex(Class cls, String mthd) {
        obj = null;
        reflex = null;
        this.mthd = _new(cls, mthd, true);
    }
    
    
    /**
     * Create a reflex from instance method.
     * @param obj object containing the method
     * @param mthd name of the method
     */
    public Reflex(Object obj, String mthd) {
        reflex = null;
        this.obj = obj;
        this.mthd = _new(obj.getClass(), mthd, false);
    }
    
    
    /**
     * Tell reflex speed (fast / slow). <p>
     * A fast reflex is invoked synchronously,
     * slow reflex - asynchronously. </p>
     * @param speed speed of reflex
     * @return reflex for chaining
     */
    public Reflex speed(String speed) {
        slow = speed.equalsIgnoreCase("slow");
        return this;
    }
    
    
    /**
     * Get reflex speed (fast / slow).  <p>
     * A fast reflex is invoked synchronously,
     * slow reflex - asynchronously. </p>
     * @return speed of reflex
     */
    public String speed() {
        return slow? "slow" : "fast";
    }
    
    
    /**
     * Invoke the response.
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    @Override
    public void on(final String stimulus, final Map args) {
        if(!slow) _on(stimulus, args);
        else threads.submit(new Thread(new Runnable() {
            @Override
            public void run() {
                _on(stimulus, args);
            }
        }));
    }
}
