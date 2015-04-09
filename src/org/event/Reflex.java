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
 * response is run on a separate thread from threadpool. </p>
 * @author wolfram77
 */
public class Reflex implements Reflexive {
    
    // data
    final Reflexive reflex;
    boolean slow;
    
    // static data
    static final ExecutorService threads = Executors.newCachedThreadPool((r) -> {
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
     * Create reflex from method.
     * @param obj object of method
     * @param cls class of method
     * @param mthd name of method
     * @param gethandle is method handle required?
     */
    private Reflexive _onMethod(Object obj, Class<?> cls, String mthd, boolean gethandle) {
        try {
            Method m = cls.getMethod(mthd, String.class, Map.class);
            boolean isstatic = Modifier.isStatic(m.getModifiers());
            if(isstatic != (obj==null)) throw new NoSuchMethodException("Method ["+m.getName()+"] is inaccessible");
            if(m.isAnnotationPresent(Speed.class)) speed(m.getAnnotation(Speed.class).value());
            if(!gethandle) return null;
            // convert to lambda expression
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodType getter = MethodType.methodType(void.class, String.class, Map.class);
            MethodType invType = MethodType.methodType(Reflexive.class, cls);
            MethodHandle target = lookup.findVirtual(cls, mthd, getter);
            CallSite site = LambdaMetafactory.metafactory(lookup, "on", invType, getter, target, getter);
            MethodHandle factory = site.getTarget();
            if(obj != null) factory = factory.bindTo(obj);
            return (Reflexive)factory.invoke();
        }
        catch(Throwable e) { throw new RuntimeException(e); }
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
        mthd = null;
        this.reflex = reflex;
        _new(reflex.getClass(), "on", false, false);
    }
    
    
    /**
     * Create reflex from static method.
     * @param cls class containing the method
     * @param mthd name of the method
     */
    public Reflex(Class cls, String mthd) {
        reflex = null;
        this.mthd = _new(cls, mthd, true, true);
    }
    
    
    /**
     * Create a reflex from instance method.
     * @param obj object containing the method
     * @param mthd name of the method
     */
    public Reflex(Object obj, String mthd) {
        reflex = null;
        this.mthd = _new(obj.getClass(), mthd, false, true).bindTo(obj);
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
        else threads.submit(new Thread(() -> {
            _on(stimulus, args);
        }));
    }
}
