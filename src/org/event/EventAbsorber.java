// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.lang.invoke.*;
import java.lang.reflect.*;



public class EventAbsorber implements IEventAbsorber {
    
    // data
    Object object;
    Method method;
    MethodHandle methodHandle;
    
    
    // EventAbsorber (cls, mthd)
    // - create an event absorber (static)
    @SuppressWarnings("UseSpecificCatch")
    public EventAbsorber(Class cls, String mthd) {
        try {
            method = cls.getMethod(mthd, String.class, Map.class);
            methodHandle = MethodHandles.lookup().unreflect(method);
        }
        catch(Exception e) { EventException.exit(e); }
    }
    
    
    // EventAbsorber (obj, mthd)
    // - create an event absorber (instance)
    public EventAbsorber(Object obj, String mthd) {
        this(obj.getClass(), mthd);
        object = obj;
    }
    
    
    // Object ()
    // - get object associated with event absorber
    public Object object() {
        return object;
    }
    
    
    // Method ()
    // - get method associated with event absorber
    public Method method() {
        return method;
    }
    
    
    // MethodHandle ()
    // - get method handle associated with event absorber
    public MethodHandle methodHandle() {
        return methodHandle;
    }
    
    
    // Absorb (event, args)
    // - absorbs an event and forwards it to method
    @Override
    public void absorb(String event, Map args) {
        try {
            if(object == null) methodHandle.invokeExact(event, args);
            else methodHandle.invoke(object, event, args);
        }
        catch(Throwable e) { EventException.exit(e); }
    }
}
