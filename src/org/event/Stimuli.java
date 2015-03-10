// @wolfram77
package org.event;

// required modules
import org.data.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.concurrent.*;



public class Stimuli extends ConcurrentHashMap<String, Set<Reactable>> implements Runnable {
    
    // data
    String stimulus;
    Map args;
    
    // static data
    static Reactable fallback = new DefReaction();
    static ExecutorService threads = Executors.newCachedThreadPool();
    
    
    // _ToHyphenCase (str)
    // - convert a string to hyphen case
    String _toHyphenCase(String str) {
        StringBuilder s = new StringBuilder();
        for(int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if(c >= 'A' && c<='Z') {
                if(i > 0) s.append('-');
                s.append((char)(c-'A'+'a'));
            }
            else s.append(c);
        }
        return s.toString();
    }
    
    
    // _OnClass (obj, cls)
    // - add static / instance methods of a class as eventers
    private void _onClass(Object obj, Class cls) {
        boolean bestatic = obj==null;
        for(Method m : cls.getMethods()) {
            // need static or instance?
            String mthd = m.getName();
            boolean isstatic = Modifier.isStatic(m.getModifiers());
            if(!mthd.startsWith("on") || ((!isstatic) && bestatic)) continue;
            // save appropriately
            String event = _toHyphenCase(mthd.substring(2));
            if(bestatic) on(event, new Reaction(cls, mthd));
            else on(event, new Reaction(obj, mthd));
        }
    }
    
    
    // _InitEventSet (event)
    // - initialize event set before adding
    void _initEventSet(String event) {
        if(get(event) != null) return;
        put(event, Collections.newSetFromMap(new ConcurrentHashMap<Reactable, Boolean>()));
    }
    
    
    // Emit (event, args)
    // - emit an event
    void _emit(String event, Map args) {
        Set<Reactable> eventers = get(event);
        if(eventers == null) fallback.on(event, args);
        else eventers.stream().forEach((e) -> {
            e.on(event, args);
        });
    }
    
    
    // Fallback ()
    // - get fallback eventer
    public static Reactable fallback() {
        return fallback;
    }
    
    
    // Fallback (eventer)
    // - set fallback eventer
    public static void fallback(Reactable eventer) {
        fallback = eventer;
    }
    
    
    // Stimuli ()
    // - create event emitter
    public Stimuli() {
    }
    
    
    // Stimuli (cls)
    // - create event emitter from class
    public Stimuli(Class cls) {
        _onClass(null, cls);
    }
    
    
    // Stimuli (obj)
    // - create event emitter from object
    public Stimuli(Object obj) {
        _onClass(obj, obj.getClass());
    }
    
    public Stimuli speed(String speed) {
        stimulus = speed.equals("fast")? "" : null;
        return this;
    }
    
    public String speed() {
        return stimulus != null? "fast" : "slow";
    }
    
    
    // Emit (event, args...)
    // - emit an event
    public Stimuli emit(String event, Map args) {
        if(stimulus == null) { _emit(event, args); return this; }
        stimulus = event;
        this.args = args;
        return this;
    }
    
    
    // Emit (event, args...)
    // - emit an event
    public Stimuli emit(String event, Object... args) {
        return emit(event, Coll.map(args));
    }
    
    
    // On (event, eventer)
    // - add an eventer to an event
    public Stimuli on(String event, Reactable eventer) {
        _initEventSet(event);
        get(event).add(eventer);
        return this;
    }
    
    
    // On (event, eventers)
    // - add eventers to an event
    public Stimuli on(String event, Collection<Reactable> eventers) {
        _initEventSet(event);
        get(event).addAll(eventers);
        return this;
    }
    
    
    // On (events, eventer)
    // - add eventer to events
    public Stimuli add(Collection<String> events, Reactable eventer) {
        events.stream().forEach((event) -> {
            on(event, eventer);
        });
        return this;
    }
    
    
    // On (map)
    // - add eventers from map
    public Stimuli on(Map<String, Set<Reactable>> map) {
        map.keySet().stream().forEach((event) -> {
            on(event, map.get(event));
        });
        return this;
    }
    
    
    // Off (event, eventer)
    // - remove an eventer from an event
    public Stimuli off(String event, Reactable eventer) {
        Set<Reactable> e = get(event);
        if(e != null) e.remove(eventer);
        return this;
    }
    
    
    // Off (event, eventers)
    // - remove eventers from an event
    public Stimuli off(String event, Collection<Reactable> eventers) {
        Set<Reactable> e = get(event);
        if(e != null) e.removeAll(eventers);
        return this;
    }
    
    
    // Off (events, eventer)
    // - remove eventer from events
    public Stimuli off(Collection<String> events, Reactable eventer) {
        events.stream().forEach((event) -> {
            off(event, eventer);
        });
        return this;
    }
    
    
    // Off (event, eventesr)
    // - remove eventers from map
    public Stimuli off(Map<String, Set<Reactable>> map) {
        map.keySet().stream().forEach((event) -> {
            off(event, map.get(event));
        });
        return this;
    }
    
    
    // Off (event)
    // - remove all eventers of an event
    public Stimuli off(String event) {
        remove(event);
        return this;
    }
    
    
    // Off ()
    // - remove all eventers
    public Stimuli off() {
        clear();
        return this;
    }
    
    @Override
    public void run() {
        _emit(stimulus, args);
    }
}
