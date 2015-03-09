// @wolfram77
package com.event;

// required modules
import java.util.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.concurrent.*;



public class Eventer {
    
    // data
    Object obj;
    MethodHandle mthd;
    Map<String, Set<Eventer>> absorbers;
    
    public Eventer() {
        absorbers = new ConcurrentHashMap<>();
    }
    
    @SuppressWarnings("UseSpecificCatch")
    public Eventer(Class cls, String mthd) {
        try {
            Method m = cls.getMethod(mthd, String.class, Map.class);
            this.mthd = MethodHandles.lookup().unreflect(m);
        }
        catch(Exception e) { new EventException(e).exit(); }
    }
}
