// @wolfram77
package org.event;

// required modules
import org.data.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.concurrent.*;



/**
 * Represents a set of stimulus with associated reactions
 * @author wolfram77
 */
public class Stimuli extends ConcurrentHashMap<String, Set<Reactable>> implements Runnable {
    
    // data
    String stimulus;
    Map args;
    
    // static data
    static Reactable fallback = new DefReaction();
    static ExecutorService threads = Executors.newCachedThreadPool();
    
    
    /**
     * Convert a string from camel case to hyphen case
     * @param str camel case string
     * @return hyphen case string
     */
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
    
    
    /**
     * Add static / instance reaction methods of a class
     * @param obj object containing methods (null if static)
     * @param cls class containing methods
     */
    private void _onClass(Object obj, Class cls) {
        boolean bestatic = obj==null;
        for(Method m : cls.getMethods()) {
            // need static or instance?
            String mthd = m.getName();
            boolean isstatic = Modifier.isStatic(m.getModifiers());
            if(!mthd.startsWith("on") || (!isstatic && bestatic)) continue;
            // save appropriately
            String stim = _toHyphenCase(mthd.substring(2));
            Reaction reaction = bestatic? new Reaction(cls, mthd) : new Reaction(obj, mthd);
            if(m.isAnnotationPresent(Reacts.class)) reaction.speed(m.getAnnotation(Reacts.class).speed());
            on(stim, reaction);
        }
    }
    
    
    /**
     * Initialize stimulus set before adding
     * @param stimulus name of stimulus
     */
    void _initStimulusSet(String stimulus) {
        if(get(stimulus) != null) return;
        put(stimulus, Collections.newSetFromMap(new ConcurrentHashMap<Reactable, Boolean>()));
    }
    
    
    // Emit (event, args)
    // - emit an event
    void _is(String stimulus, Map args) {
        Set<Reactable> eventers = get(stimulus);
        if(eventers == null) fallback.on(stimulus, args);
        else eventers.stream().forEach((e) -> {
            e.on(stimulus, args);
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

    
    @Override
    public void run() {
        _is(stimulus, args);
    }
    
    
    // Emit (event, args...)
    // - emit an event
    public Stimuli emit(String event, Map args) {
        if(stimulus == null) { _is(event, args); return this; }
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
        _initStimulusSet(event);
        get(event).add(eventer);
        return this;
    }
    
    
    // On (event, eventers)
    // - add eventers to an event
    public Stimuli on(String event, Collection<Reactable> eventers) {
        _initStimulusSet(event);
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
}