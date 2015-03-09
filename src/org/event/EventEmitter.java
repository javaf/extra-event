// @wolfram77
package org.event;

// required modules
import org.data.*;
import java.util.*;
import java.util.concurrent.*;



public class EventEmitter extends ConcurrentHashMap<String, Set<Eventable>> {
    
    // static data
    public static Eventable fallback = new DefEventer();
    
    
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
    
    
    // Add (event, eventer)
    // - add an eventer to an event
    public EventEmitter add(String event, Eventable absorber) {
        if(get(event) == null) put(event, Collections.newSetFromMap(new ConcurrentHashMap<Eventable, Boolean>()));
        get(event).add(absorber);
        return this;
    }

    
    // Remove (event, eventer)
    // - remove an eventer from an event
    public EventEmitter remove(String event, Eventable absorber) {
        Set<Eventable> absb = get(event);
        if(absb != null) absb.remove(absorber);
        return this;
    }
    
    
    // Remove (event)
    // - remove all eventer of an event
    public EventEmitter remove(String event) {
        super.remove(event);
        return this;
    }
    
    
    // Remove ()
    // - remove all event eventer
    public EventEmitter remove() {
        clear();
        return this;
    }
}