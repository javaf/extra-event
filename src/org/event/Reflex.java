// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.lang.invoke.*;
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
    final MethodHandle mthd;
    final Reflexive reflex;
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
    
    
    /**
     * Create reflex from method.
     * @param cls class of method
     * @param mthd name of method
     * @param bestatic should method be static?
     * @param gethandle is method handle required?
     */
    private MethodHandle _new(Class cls, String mthd, boolean bestatic, boolean gethandle) throws ReflectiveOperationException {
        Method m = cls.getMethod(mthd, String.class, Map.class);
        boolean isstatic = Modifier.isStatic(m.getModifiers());
        if(isstatic != bestatic) throw new NoSuchMethodException("Method ["+m.getName()+"] is inaccessible");
        if(m.isAnnotationPresent(Speed.class)) speed(m.getAnnotation(Speed.class).value());
        return gethandle? MethodHandles.lookup().unreflect(m) : null;
    }


    /**
     * Invoke reflex synchronously.
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void _on(String stimulus, Map args) {
        try {
            if(mthd == null) reflex.on(stimulus, args);
            else mthd.invoke(stimulus, args);
        }
        catch(Throwable e) { throw new RuntimeException(e); }
    }
    
    
    /**
     * Encapsulate a reflex. <p>
     * Slow reflex must be encapsulated. </p>
     * @param reflex reflex to encapsulate
     */
    public Reflex(Reflexive reflex) {
        this.mthd = null;
        this.reflex = reflex;
        try { _new(reflex.getClass(), "on", false, false); }
        catch(ReflectiveOperationException e) {}
    }
    
    
    /**
     * Create reflex from static method.
     * @param cls class containing the method
     * @param mthd name of the method
     * @throws java.lang.ReflectiveOperationException if method not accessible
     */
    public Reflex(Class cls, String mthd) throws ReflectiveOperationException {
        this.reflex = null;
        this.mthd = _new(cls, mthd, true, true);
    }
    
    
    /**
     * Create a reflex from instance method.
     * @param obj object containing the method
     * @param mthd name of the method
     * @throws java.lang.ReflectiveOperationException if method not accessible
     */
    public Reflex(Object obj, String mthd) throws ReflectiveOperationException {
        this.reflex = null;
        this.mthd = _new(obj.getClass(), mthd, false, true).bindTo(obj);
    }
    
    
    /**
     * Tell reflex speed <i>fast / slow.</i> <p>
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
     * Get reflex speed <i>fast / slow.</i>  <p>
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
