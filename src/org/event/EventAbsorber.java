// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.lang.invoke.*;
import java.lang.reflect.*;



public class EventAbsorber implements IEventAbsorber {
    
    // data
    Object obj;
    MethodHandle hMthd;
    
    
    // EventAbsorber (cls, mthd)
    // - create an event absorber (static)
    @SuppressWarnings("UseSpecificCatch")
    public EventAbsorber(Class cls, String mthd) {
        try {
            Method _mthd = cls.getMethod(mthd, String.class, Map.class);
            hMthd = MethodHandles.lookup().unreflect(_mthd);
        }
        catch(Exception e) { EventException.exit(e); }
    }
    
    
    // EventAbsorber (obj, mthd)
    // - create an event absorber (instance)
    public EventAbsorber(Object obj, String mthd) {
        this(obj.getClass(), mthd);
        this.obj = obj;
    }
    
    
    // Absorb (event, args)
    // - absorbs an event and forwards it to method
    @Override
    public void absorb(String event, Map args) {
        try {
            if(obj == null) hMthd.invokeExact(event, args);
            else hMthd.invoke(obj, event, args);
        }
        catch(Throwable e) { EventException.exit(e); }
    }
}
