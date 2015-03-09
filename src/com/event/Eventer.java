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
    Map<String, Set<Eventer>> eventers;
    
    
    // _ToHyphenCase (str)
    // - convert a string to hyphen case
    String _toHyphenCase(String str) {
        StringBuilder s = new StringBuilder();
        for(int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if(c >= 'A' && c<='Z') {
                if(i > 0) s.append('-');
                s.append(c-'A'+'a');
            }
            else s.append(c);
        }
        return s.toString();
    }
    
    void _runAll(String event, Map args) {
        try {
            if(obj != null) mthd.invoke(obj, event, args);
            else mthd.invokeExact(event, args);
        }
        catch(Throwable e) { new EventException(e).exit(); }
    }
    
    void _runOn(String event, Map args) {
        Set<Eventer> seventers = eventers.get(event);
        if(seventers == null) return;
        seventers.stream().forEach((eventer) -> { 
            eventer.emit(event, args);
        });
    }
    
    public Eventer() {
        eventers = new ConcurrentHashMap<>();
    }
    
    @SuppressWarnings("UseSpecificCatch")
    public Eventer(Class cls, String mthd) {
        this();
        try {
            Method m = cls.getMethod(mthd, String.class, Map.class);
            this.mthd = MethodHandles.lookup().unreflect(m);
        }
        catch(Exception e) { new EventException(e).exit(); }
    }
    
    public Eventer(Object obj, String mthd) {
        this(obj.getClass(), mthd);
        this.obj = obj;
    }

    public void on(Class cls) {
        for(Method mthd : cls.getMethods()) {
            boolean isstatic = Modifier.isStatic(mthd.getModifiers());
            if(!isstatic || !mthd.getName().startsWith("on")) continue;
            String event = _toHyphenCase(mthd.getName().substring(2));
            eventers.put(event, null);
        }
    }
    
    public void emit(String event, Map args) {
        if(mthd != null) _runAll(event, args);
        if(eventers != null) _runOn(event, args);
    }
    
}
