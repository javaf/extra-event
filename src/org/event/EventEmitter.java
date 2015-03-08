// @wolfram77
package org.event;

// required modules
import org.data.*;
import java.util.*;
import java.util.concurrent.*;



public class EventEmitter extends ConcurrentHashMap<String, Set<IEventAbsorber>> {
    
    // static data
    public static IEventAbsorber fallback = new DefaultEventAbsorber();
    
    
    // Emit (event, args)
    // - emit a event to all absorbers
    public EventEmitter emit(String event, Map args) {
        Set<IEventAbsorber> absorbers = get(event);
        if(absorbers == null) fallback.absorb(event, args);
        else absorbers.stream().forEach((e) -> {
            e.absorb(event, args);
        });
        return this;
    }
    
    
    // Emit (event, args...)
    // - emit a event to all absorbers
    public EventEmitter emit(String event, Object... args) {
        return emit(event, Coll.map(args));
    }
    
    
    // Add (event, absorber)
    // - add an absorber to an event
    public EventEmitter add(String event, IEventAbsorber absorber) {
        if(get(event) == null) put(event, Collections.newSetFromMap(new ConcurrentHashMap<IEventAbsorber, Boolean>()));
        get(event).add(absorber);
        return this;
    }

    
    // Remove (event, absorber)
    // - remove an absorber from an event
    public EventEmitter remove(String event, IEventAbsorber absorber) {
        Set<IEventAbsorber> absb = get(event);
        if(absb != null) absb.remove(absorber);
        return this;
    }
    
    
    // Remove (event)
    // - remove all absorbers of an event
    public EventEmitter remove(String event) {
        super.remove(event);
        return this;
    }
    
    
    // Remove ()
    // - remove all event absorbers
    public EventEmitter remove() {
        clear();
        return this;
    }
}
