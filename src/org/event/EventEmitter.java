// @wolfram77
package org.event;

// required modules
import org.data.*;
import java.util.*;



public class EventEmitter extends HashMap<String, Set<IEventAbsorber>> {
    
    // data
    IEventAbsorber fallback;
    
    
    // Emit (event, args)
    // - emit a event to all absorbers with specified fallback
    void emit(IEventAbsorber fb, String event, Map args) {
        Set<IEventAbsorber> absb = get(event);
        if(absb == null) { if(fb != null) fb.absorb(event, args); return; }
        for(IEventAbsorber e : absb)
            e.absorb(event, args);
    }
    
    
    // EventEmitter ()
    // - create an event emitter
    public EventEmitter() {
        fallback = new DefaultEventAbsorber();
    }
    
    
    // Fallback ()
    // - get fallback event absorber
    public IEventAbsorber fallback() {
       return fallback; 
    }
    
    
    // Fallback (e)
    // - set fallback event absorber
    public EventEmitter fallback(IEventAbsorber e) {
        fallback = e;
        return this;
    }
    
    
    // Emit (event, args)
    // - emit a event to all absorbers
    public EventEmitter emit(String event, Map args) {
        emit(fallback, event, args);
        return this;
    }
    
    
    // Emit (e, event, args...)
    // - emit a event to all absorbers
    public EventEmitter emit(String event, Object... args) {
        return emit(event, Coll.map(args));
    }
    
    
    public Set<IEventAbsorber> put(String event, Set<IEventAbsorber> absorber) {
        return null;
    }
    
    
    // Add (event, absorber)
    // - add an absorber to an event
    public EventEmitter add(String event, IEventAbsorber absorber) {
        if(get(event) == null) put(event, new HashSet<>());
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
