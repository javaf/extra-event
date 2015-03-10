// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.lang.invoke.*;
import java.lang.reflect.*;



public class Reaction implements Reactable {
    
    // data
    Object obj;
    MethodHandle mthd;
    String stimulus;
    Map args;
    
    
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
    
    // Reaction (cls, mthd)
    // - create an eventer (static)
    public Reaction(Class cls, String mthd) {
        _new(cls, mthd, true);
    }
    
    
    // Reaction (obj, mthd)
    // - create an eventer (instance)
    public Reaction(Object obj, String mthd) {
        _new(obj.getClass(), mthd, false);
        this.obj = obj;
    }
    
    public Reaction speed(String speed) {
        stimulus = speed.equals("slow")? "" : null;
        return this;
    }
    
    public String speed() {
        return stimulus != null? "slow" : "fast";
    }
    
    // On (event, args)
    // - listens to an event and forwards it to method
    @Override
    public void on(String event, Map args) {
        try {
            if(mthd == null) ((Reactable)obj).on(event, args);
            else if(obj == null) mthd.invokeExact(event, args);
            else mthd.invoke(obj, event, args);
        }
        catch(Throwable e) { new StimuliException(e).exit(); }
    }
}
