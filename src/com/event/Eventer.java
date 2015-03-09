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
    
    
    // _RunAll (event, args)
    // - run the all-case method
    void _runAll(String event, Map args) {
        try {
            if(obj != null) mthd.invoke(obj, event, args);
            else mthd.invokeExact(event, args);
        }
        catch(Throwable e) { new EventException(e).exit(); }
    }
    
    
    // _RunOn (event, args)
    // - run specific on-case methods
    void _runOn(String event, Map args) {
        Set<Eventer> seventers = eventers.get(event);
        if(seventers == null) return;
        seventers.stream().forEach((eventer) -> { 
            eventer.emit(event, args);
        });
    }
    
    
    void _on(Object obj, Class cls) {
        boolean bestatic = obj==null;
        for(Method m : cls.getMethods()) {
            // need static or instance?
            String name = m.getName();
            boolean isstatic = Modifier.isStatic(m.getModifiers());
            if(isstatic!=bestatic || !name.startsWith("on")) continue;
            // save appropriately
            String event = _toHyphenCase(name.substring(2));
            if(bestatic) on(event, new Eventer(cls, name));
            else on(event, new Eventer(obj, name));
        }
    }
    
    
    // Eventer ()
    // - create new eventer
    public Eventer() {
        eventers = new ConcurrentHashMap<>();
    }
    
    
    // Eventer (cls, mthd)
    // - create new eventer with static all-case method
    @SuppressWarnings("UseSpecificCatch")
    public Eventer(Class cls, String mthd) {
        this();
        try {
            Method m = cls.getMethod(mthd, String.class, Map.class);
            this.mthd = MethodHandles.lookup().unreflect(m);
        }
        catch(Exception e) { new EventException(e).exit(); }
    }
    
    
    // Eventer (obj, mthd)
    // - create new eventer with instance all-case method
    public Eventer(Object obj, String mthd) {
        this(obj.getClass(), mthd);
        this.obj = obj;
    }
    
    public void on(String event, Eventer eventer) {
        if(eventers == null) eventers = new ConcurrentHashMap<>();
        if(eventers.get(event) == null) eventers.put(event, Collections.newSetFromMap(new ConcurrentHashMap<Eventer, Boolean>()));
        eventers.get(event).add(eventer);
    }
    
    public void on(String event, Collection<Eventer> eventers) {
        eventers.stream().forEach((eventer) -> {
            on(event, eventer);
        });
    }
    
    public void on(Eventer eventer, boolean imprt) {
        eventer.eventers.keySet().stream().forEach((event) -> {
            if(!imprt) on(event, eventer);
            else on(event, eventer.eventers.get(event));
        });
    }
    
    public void on(Eventer eventer) {
        on(eventer, false);
    }
    
    public void on(Class cls) {
        _on(null, cls);
    }
    
    public void on(Object obj) {
        _on(obj, obj.getClass());
    }
    
    public void notOn(String event, Eventer eventer) {
        if(eventers == null) return;
        if(eventers.get(event) == null) return;
        eventers.get(event).remove(eventer);
    }
    
    public void notOn(String event) {
        if(eventers == null) return;
        eventers.remove(event);
    }
    
    public void notOn() {
        if(eventers == null) return;
        eventers.clear();
        eventers = null;
    }
    
    
    // Emit (event, args)
    // - emit an event through eventer
    public void emit(String event, Map args) {
        if(mthd != null) _runAll(event, args);
        if(eventers != null) _runOn(event, args);
    }
}
