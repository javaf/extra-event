// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.concurrent.*;



/**
 * <b>Reaction to a stimulus</b>
 * <div>Encapsulates a reaction-method or {@linkplain Reactable} object</div>
 * @author wolfram77
 */
public class React implements Reactable, Runnable {
    
    // data
    Object obj;
    MethodHandle mthd;
    String stimulus;
    Map args;
    
    // static data
    static ExecutorService threads = Spine.threads;
    
    
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
            if(m.isAnnotationPresent(Reacts.class) && m.getAnnotation(Reacts.class).value().equalsIgnoreCase("slow")) stimulus = "";
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
            if(mthd == null) ((Reactable)obj).on(stimulus, args);
            else if(obj == null) mthd.invokeExact(stimulus, args);
            else mthd.invoke(obj, stimulus, args);
        }
        catch(Throwable e) { throw new SpineException(e); }
    }

    
    /**
     * <b>Create a copy of another reaction</b>
     * @param reaction reaction to copy
     */
    public Reaction(Reaction reaction) {
        obj = reaction.obj;
        mthd = reaction.mthd;
        stimulus = reaction.stimulus;
    }

    
    /**
     * <b>Create a reaction from an object implementing {@linkplain Reactable}</b>
     * @param reactable object implementing {@linkplain Reactable}
     */
    public Reaction(Reactable reactable) {
        obj = reactable;
        _new(reactable.getClass(), "on", false);
    }
    
    
    /**
     * <b>Create a reaction from a static reaction-method</b>
     * @param cls class containing reaction-method
     * @param mthd name of reaction-method
     */
    public Reaction(Class cls, String mthd) {
        this.mthd = _new(cls, mthd, true);
    }
    
    
    /**
     * <b>Create a reaction from an instance reaction-method</b>
     * @param obj object containing reaction-method
     * @param mthd name of reaction-method
     */
    public Reaction(Object obj, String mthd) {
        this.mthd = _new(obj.getClass(), mthd, false);
        this.obj = obj;
    }
    
    
    /**
     * <b>Tell reaction-method's speed ("fast" or "slow")</b>
     * <div>A fast reaction-method is invoked synchronously</div>
     * <div>A slow reaction-method is invoked asynchronously</div>
     * @param speed reaction-method's speed
     * @return {@linkplain Reaction} for chaining
     */
    public Reaction speed(String speed) {
        stimulus = speed.equalsIgnoreCase("slow")? "" : null;
        return this;
    }
    
    
    /**
     * <b>Get reaction-method's speed ("fast" or "slow")</b>
     * <div>A fast reaction-method is invoked synchronously</div>
     * <div>A slow reaction-method is invoked asynchronously</div>
     * @return reaction-method's speed
     */
    public String speed() {
        return stimulus != null? "slow" : "fast";
    }
    
    
    /**
     * <b>Invoke the reaction-method</b>
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
     * <b>Invoke reaction-method asynchronously</b>
     * <div>DONT CALL THIS!</div>
     */
    @Override
    public void run() {
        _on(stimulus, args);
    }
}
