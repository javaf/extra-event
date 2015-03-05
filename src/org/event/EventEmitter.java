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
        Set<IEventAbsorber> lstn = get(event);
        if(lstn == null) { if(fb != null) fb.absorb(event, args); return; }
        for(IEventAbsorber e : lstn)
            e.absorb(event, args);
    }
    
    
    // EventEmitter ()
    // - create an event emitter
    public EventEmitter() {
        fallback = new DefaultEventListener();
    }
    
    
    // Fallback ()
    // - get fallback event listener
    public IEventListener fallback() {
       return fallback; 
    }
    
    
    // Fallback (e)
    // - set fallback event listener
    public EventEmitter fallback(IEventListener e) {
        fallback = e;
        return this;
    }
    
    
    // Emit (event, args)
    // - emit a event to all listeners
    public EventEmitter emit(String event, Map args) {
        emit(fallback, event, args);
        return this;
    }
    
    
    // Emit (e, event, args...)
    // - emit a event to all listeners
    public EventEmitter emit(String event, Object... args) {
        return emit(event, Coll.map(args));
    }
    
    
    // Add (event, listener)
    // - add a listener to an event
    public EventEmitter add(String event, IEventListener listener) {
        if(get(event) == null) put(event, new HashSet<IEventListener>());
        get(event).add(listener);
        return this;
    }

    
    // Remove (event, listener)
    // - remove a listener from an event
    public EventEmitter remove(String event, IEventListener listener) {
        Set<IEventListener> lstn = get(event);
        if(lstn != null) lstn.remove(listener);
        return this;
    }
    
    
    // Remove (event, listener)
    // - remove all listener of an event
    public EventEmitter remove(String event) {
        super.remove(event);
        return this;
    }
    
    
    // Remove (event, listener)
    // - remove all event listeners
    public EventEmitter remove() {
        clear();
        return this;
    }
}
