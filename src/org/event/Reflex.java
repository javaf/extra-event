// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.concurrent.*;



/**
 * <b>Response to a stimulus</b>
 * <div>Encapsulates a fast or <em>slow</em> response</div>
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
     * <b>Create reflex from method</b>
     * @param cls class of method
     * @param mthd name of method
     * @param bestatic should method be static?
     * @param gethandle is method handle required?
     */
    private MethodHandle _new(Class cls, String mthd, boolean bestatic, boolean gethandle) throws ReflectiveOperationException {
        Method m = cls.getMethod(mthd, String.class, Map.class);
        boolean isstatic = Modifier.isStatic(m.getModifiers());
        if(!isstatic && bestatic) throw new NoSuchMethodException("Method ["+m.getName()+"] is not static");
        if(m.isAnnotationPresent(Speed.class)) speed(m.getAnnotation(Speed.class).value());
        return gethandle? MethodHandles.lookup().unreflect(m) : null;
    }


    /**
     * <b>Invoke reflex synchronously</b>
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
     * <b>Encapsulate a reflex</b>
     * <div>Slow reflex must be encapsulated</div>
     * @param reflex reflex to encapsulate
     */
    public Reflex(Reflexive reflex) {
        this.mthd = null;
        this.reflex = reflex;
        try { _new(reflex.getClass(), "on", false, false); }
        catch(ReflectiveOperationException e) {}
    }
    
    
    /**
     * <b>Create reflex from a static method</b>
     * @param cls class containing the method
     * @param mthd name of the method
     * @throws java.lang.ReflectiveOperationException if method not accessible
     */
    public Reflex(Class cls, String mthd) throws ReflectiveOperationException {
        this.reflex = null;
        this.mthd = _new(cls, mthd, true, true);
    }
    
    
    /**
     * <b>Create a reflex from an instance method</b>
     * @param obj object containing the method
     * @param mthd name of the method
     * @throws java.lang.ReflectiveOperationException if method not accessible
     */
    public Reflex(Object obj, String mthd) throws ReflectiveOperationException {
        this.reflex = null;
        this.mthd = _new(obj.getClass(), mthd, false, true);
        this.mthd.bindTo(obj);
    }
    
    
    /**
     * <b>Tell reflex speed ("fast" or "slow")</b>
     * <div>A fast reflex is invoked synchronously</div>
     * <div>A slow reflex is invoked asynchronously</div>
     * @param speed speed of reflex
     * @return {@linkplain Reflex} for chaining
     */
    public Reflex speed(String speed) {
        slow = speed.equalsIgnoreCase("slow");
        return this;
    }
    
    
    /**
     * <b>Get reflex speed ("fast" or "slow")</b>
     * <div>A fast reflex is invoked synchronously</div>
     * <div>A slow reflex is invoked asynchronously</div>
     * @return speed of reflex
     */
    public String speed() {
        return slow? "slow" : "fast";
    }
    
    
    /**
     * <b>Invoke the reflex</b>
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
