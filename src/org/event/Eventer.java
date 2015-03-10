// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.lang.invoke.*;
import java.lang.reflect.*;



public class Eventer implements Eventable {
    
    // data
    Object obj;
    MethodHandle mthd;
    
    
    // Eventer (cls, mthd)
    // - create an eventer (static)
    @SuppressWarnings("UseSpecificCatch")
    public Eventer(Class cls, String mthd) {
        try {
            Method m = cls.getMethod(mthd, String.class, Map.class);
            this.mthd = MethodHandles.lookup().unreflect(m);
        }
        catch(Exception e) { new EventException(e).exit(); }
    }
    
    
    // Eventer (obj, mthd)
    // - create an eventer (instance)
    public Eventer(Object obj, String mthd) {
        this(obj.getClass(), mthd);
        this.obj = obj;
    }
    
    
    // On (event, args)
    // - absorbs an event and forwards it to method
    @Override
    public void on(String event, Map args) {
        try {
            if(obj == null) mthd.invokeExact(event, args);
            else mthd.invoke(obj, event, args);
        }
        catch(Throwable e) { new EventException(e).exit(); }
    }
}
