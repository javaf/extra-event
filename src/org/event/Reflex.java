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
    boolean fast;
    
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
     * <b>Create a new reaction from a reaction-method</b>
     * @param cls class of reaction-method
     * @param mthd name of reaction-method
     * @param bestatic should method be static?
     */
    private MethodHandle _new(Class cls, String mthd, boolean bestatic) {
        try {
            Method m = cls.getMethod(mthd, String.class, Map.class);
            boolean isstatic = Modifier.isStatic(m.getModifiers());
            if(!isstatic && bestatic) throw new Exception("Method ["+m.getName()+"] is not static");
            if(m.isAnnotationPresent(Speed.class) && m.getAnnotation(Speed.class).value().equalsIgnoreCase("slow")) stimulus = "";
            return MethodHandles.lookup().unreflect(m);
        }
        catch(Exception e) { new SpineException(e).exit(); }
        return null;
    }


    /**
     * <b>Invoke a fast reaction-method</b>
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
     * <div>Slow reflex must be encapsuted</div>
     * @param reflex reflex to encapsulate
     */
    public Reflex(Reflexive reflex) {
        this.reflex = reflex;
        _new(reflex.getClass(), "on", false);
    }
    
    
    /**
     * <b>Create reflex from a static method</b>
     * @param cls class containing the method
     * @param mthd name of the method
     */
    public Reflex(Class cls, String mthd) {
        this.mthd = _new(cls, mthd, true);
    }
    
    
    /**
     * <b>Create a reflex from an instance method</b>
     * @param obj object containing the method
     * @param mthd name of the method
     */
    public Reflex(Object obj, String mthd) {
        this.mthd = _new(obj.getClass(), mthd, false);
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
        stimulus = speed.equalsIgnoreCase("slow")? "" : null;
        return this;
    }
    
    
    /**
     * <b>Get reflex speed ("fast" or "slow")</b>
     * <div>A fast reflex is invoked synchronously</div>
     * <div>A slow reflex is invoked asynchronously</div>
     * @return speed of reflex
     */
    public String speed() {
        return stimulus != null? "slow" : "fast";
    }
    
    
    /**
     * <b>Invoke the reflex</b>
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    @Override
    public void on(final String stimulus, final Map args) {
        if(fast) _on(stimulus, args);
        else threads.submit(new Thread(new Runnable() {
            @Override
            public void run() {
                _on(stimulus, args);
            }
        }));
    }
    
    
    /**
     * <b>Invoke reaction-method asynchronously</b>
     * <div>DONT CALL THIS!</div>
     */
    @Override
    public void run() {
        _on(stimulus, args);
    }
}