// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.concurrent.*;



/**
 * <h3>Reaction to a stimulus</h3>
 * Encapsulates a reaction-method or {@linkplain Reactable} object
 * @author wolfram77
 */
public class Reaction implements Reactable, Runnable {
    
    // data
    Object obj;
    MethodHandle mthd;
    String stimulus;
    Map args;
    
    // static data
    static ExecutorService threads = Executors.newCachedThreadPool();
    
    
    /**
     * <h3>Create a new reaction from a reaction-method</h3>
     * @param cls class of reaction-method
     * @param mthd name of reaction-method
     * @param bestatic should method be static?
     */
    private void _new(Class cls, String mthd, boolean bestatic) {
        try {
            Method m = cls.getMethod(mthd, String.class, Map.class);
            boolean isstatic = Modifier.isStatic(m.getModifiers());
            if(!isstatic && bestatic) throw new Exception("Method ["+m.getName()+"] is not static");
            if(m.isAnnotationPresent(Reacts.class) && m.getAnnotation(Reacts.class).speed().equals("slow")) stimulus = "";
            this.mthd = MethodHandles.lookup().unreflect(m);
        }
        catch(Exception e) { new SpineException(e).exit(); }
    }


    /**
     * <h3>Invoke a fast reaction-method</h3>
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void _on(String stimulus, Map args) {
        try {
            if(mthd == null) ((Reactable)obj).on(stimulus, args);
            else if(obj == null) mthd.invokeExact(stimulus, args);
            else mthd.invoke(obj, stimulus, args);
        }
        catch(Throwable e) { throw new SpineException(e); }
    }

    
    /**
     * <h3>Create a copy of another reaction</h3>
     * @param reaction reaction to copy
     */
    public Reaction(Reaction reaction) {
        obj = reaction.obj;
        mthd = reaction.mthd;
        stimulus = reaction.stimulus;
    }

    
    /**
     * <h3>Create a reaction from an object implementing {@linkplain Reactable}</h3>
     * @param reactable object implementing {@linkplain Reactable}
     */
    public Reaction(Reactable reactable) {
        obj = reactable;
    }
    
    
    /**
     * <h3>Create a reaction from a static reaction-method</h3>
     * @param cls class containing reaction-method
     * @param mthd name of reaction-method
     */
    public Reaction(Class cls, String mthd) {
        _new(cls, mthd, true);
    }
    
    
    /**
     * <h3>Create a reaction from an instance reaction-method</h3>
     * @param obj object containing reaction-method
     * @param mthd name of reaction-method
     */
    public Reaction(Object obj, String mthd) {
        _new(obj.getClass(), mthd, false);
        this.obj = obj;
    }
    
    
    /**
     * <h3>Tell reaction-method's speed ("fast" or "slow")</h3>
     * A fast reaction-method is invoked synchronously <br/>
     * A slow reaction-method is invoked asynchronously
     * @param speed reaction-method's speed
     * @return {@linkplain Reaction} for chaining
     */
    public Reaction speed(String speed) {
        stimulus = speed.equals("slow")? "" : null;
        return this;
    }
    
    
    /**
     * <h3>Get reaction-method's speed ("fast" or "slow")</h3>
     * A fast reaction-method is invoked synchronously <br/>
     * A slow reaction-method is invoked asynchronously
     * @return reaction-method's speed
     */
    public String speed() {
        return stimulus != null? "slow" : "fast";
    }
    
    
    /**
     * <h3>Invoke the reaction-method</h3>
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    @Override
    public void on(String stimulus, Map args) {
        if(this.stimulus == null) { _on(stimulus, args); return; }
        this.stimulus = stimulus;
        this.args = args;
        threads.submit(this);
    }
    
    
    /**
     * <h3>Invoke reaction-method asynchronously</h3>
     * DONT CALL THIS!
     */
    @Override
    public void run() {
        _on(stimulus, args);
    }
}
