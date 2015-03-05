// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.lang.invoke.*;
import java.lang.reflect.*;



public class EventMethod {
    
    // data
    Object object;
    Method method;
    MethodHandle methodHandle;
    
    public EventMethod(Class cls, String mthd) throws NoSuchMethodException, IllegalAccessException {
        method = cls.getMethod(mthd, String.class, Map.class);
        methodHandle = MethodHandles.lookup().unreflect(method);
    }
    
    public EventMethod(Object obj, String mthd) throws NoSuchMethodException, IllegalAccessException {
        this(obj.getClass(), mthd);
        object = obj;
    }
    
    public Object object() {
        return object;
    }
    
    public Method method() {
        return method;
    }
    
    public MethodHandle methodHandle() {
        return methodHandle;
    }
    
    public void invoke(String event, Map args) throws Throwable {
        if(object == null) methodHandle.invokeExact(object, event, args);
        else methodHandle.invokeExact(object, event, args);
    }
}
