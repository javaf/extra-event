// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.concurrent.*;



/**
 * Reaction to a stimulus
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
     * Creates a new reaction from a method
     * @param cls class of method
     * @param mthd name of method
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
        catch(Exception e) { throw new StimuliException(e); }
    }


    /**
     * Calls a fast reaction method
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void _on(String stimulus, Map args) {
        try {
            if(mthd == null) ((Reactable)obj).on(stimulus, args);
            else if(obj == null) mthd.invokeExact(stimulus, args);
            else mthd.invoke(obj, stimulus, args);
        }
        catch(Throwable e) { throw new StimuliException(e); }
    }

    
    /**
     * Create a reaction from an object implementing {@linkplain Reactable}
     * @param reactable object implementing {@linkplain Reactable}
     */
    public Reaction(Reactable reactable) {
        obj = reactable;
    }
    
    
    /**
     * Create a reaction from a static method
     * @param cls class containing method
     * @param mthd name of method
     */
    public Reaction(Class cls, String mthd) {
        _new(cls, mthd, true);
    }
    
    
    /**
     * Create a reaction from an instance method
     * @param obj object containing method
     * @param mthd name of method
     */
    public Reaction(Object obj, String mthd) {
        _new(obj.getClass(), mthd, false);
        this.obj = obj;
    }
    
    
    /**
     * Tell reaction method's speed ("fast" or "slow")
     * @param speed reaction method's speed
     * @return {@linkplain Reaction}
     */
    public Reaction speed(String speed) {
        stimulus = speed.equals("slow")? "" : null;
        return this;
    }
    
    
    /**
     * Get reaction method's speed ("fast" or "slow")
     * @return reaction method's speed
     */
    public String speed() {
        return stimulus != null? "slow" : "fast";
    }
    
    
    /**
     * Calls the reaction method
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    @Override
    public void on(String stimulus, Map args) {
        if(stimulus == null) { _on(stimulus, args); return; }
        this.stimulus = stimulus;
        this.args = args;
        threads.submit(this);
    }
    
    
    /**
     * DONT CALL THIS!
     * Calls reaction method asynchronously
     */
    @Override
    public void run() {
        _on(stimulus, args);
    }
}
