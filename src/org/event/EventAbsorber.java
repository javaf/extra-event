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
    
    
    // EventMethod (cls, mthd)
    // - create an event method (static)
    public EventAbsorber(Class cls, String mthd) throws NoSuchMethodException, IllegalAccessException {
        method = cls.getMethod(mthd, String.class, Map.class);
        methodHandle = MethodHandles.lookup().unreflect(method);
    }
    
    
    // EventMethod (obj, mthd)
    // - create an event method (instance)
    public EventAbsorber(Object obj, String mthd) throws NoSuchMethodException, IllegalAccessException {
        this(obj.getClass(), mthd);
        object = obj;
    }
    
    
    // Object ()
    // - object associated with event method
    public Object object() {
        return object;
    }
    
    
    // Method ()
    // - method associated with event method
    public Method method() {
        return method;
    }
    
    
    // MethodHandle ()
    // - method handle associated with event method
    public MethodHandle methodHandle() {
        return methodHandle;
    }
    
    
    // Listen (event, args)
    // - accepts listen call
    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void absorb(String event, Map args) {
        try {
            if(object == null) methodHandle.invokeExact(object, event, args);
            else methodHandle.invokeExact(object, event, args);
        }
        catch(Throwable e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
