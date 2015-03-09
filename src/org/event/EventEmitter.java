// @wolfram77
package org.event;

// required modules
import org.data.*;
import java.util.*;
import java.util.concurrent.*;



public class EventEmitter extends ConcurrentHashMap<String, Set<Eventable>> {
    
    // static data
    public static Eventable fallback = new DefEventer();
    
    
    // _InitEventSet (event)
    // - initialize event set before adding
    void _initEventSet(String event) {
        if(get(event) != null) return;
        put(event, Collections.newSetFromMap(new ConcurrentHashMap<Eventable, Boolean>()));
    }
    
    
    // Emit (event, args)
    // - emit a event to all eventers
    public EventEmitter emit(String event, Map args) {
        Set<Eventable> eventers = get(event);
        if(eventers == null) fallback.absorb(event, args);
        else eventers.stream().forEach((e) -> {
            e.absorb(event, args);
        });
        return this;
    }
    
    
    // Emit (event, args...)
    // - emit a event to all eventers
    public EventEmitter emit(String event, Object... args) {
        return emit(event, Coll.map(args));
    }
    
    
    // On (event, eventer)
    // - add an eventer to an event
    public EventEmitter on(String event, Eventer eventer) {
        _initEventSet(event);
        get(event).add(eventer);
        return this;
    }
    
    
    // On (event, eventers)
    // - add eventers to an event
    public EventEmitter on(String event, Collection<Eventer> eventers) {
        _initEventSet(event);
        get(event).addAll(eventers);
        return this;
    }
    
    
    // On (events, eventer)
    // - add eventer to events
    public EventEmitter on(Collection<String> events, Eventer eventer) {
        events.stream().forEach((event) -> {
            on(event, eventer);
        });
        return this;
    }

    
    // NotOn (event, eventer)
    // - remove an eventer from an event
    public EventEmitter notOn(String event, Eventable eventer) {
        Set<Eventable> absb = get(event);
        if(absb != null) absb.remove(eventer);
        return this;
    }
    
    
    // NotOn (event)
    // - remove all eventers of an event
    public EventEmitter notOn(String event) {
        remove(event);
        return this;
    }
    
    
    // notOn ()
    // - remove all eventers
    public EventEmitter notOn() {
        clear();
        return this;
    }
}
