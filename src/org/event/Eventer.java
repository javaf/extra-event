// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.function.*;



public class Eventer implements Eventable {
    
    // data
    Object obj;
    MethodHandle mthd;
    Eventable lambda;
    
    
    // Eventer (cls, mthd)
    // - create an eventer (static)
    @SuppressWarnings("UseSpecificCatch")
    public Eventer(Class cls, String mthd) {
        try {
            Method m = cls.getMethod(mthd, String.class, Map.class);
            MethodHandle mh = MethodHandles.lookup().unreflect(m);
            lambda = (Eventable)LambdaMetafactory.metafactory(MethodHandles.lookup(), "absorb", MethodType.methodType(Eventable.class), mh.type(), mh, mh.type()).getTarget().invokeExact();
        }
        catch(Throwable e) { new EventException(e).exit(); }
    }
    
    
    // Eventer (obj, mthd)
    // - create an eventer (instance)
    public Eventer(Object obj, String mthd) {
        this(obj.getClass(), mthd);
        this.obj = obj;
    }
    
    
    // Absorb (event, args)
    // - absorbs an event and forwards it to method
    @Override
    public void absorb(String event, Map args) {
        try {
            if(obj == null) lambda.absorb(event, args);
            else lambda.absorb(event, args);
            // if(obj == null) mthd.invokeExact(event, args);
            // else mthd.invoke(obj, event, args);
        }
        catch(Throwable e) { new EventException(e).exit(); }
    }
}
