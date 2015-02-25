// @wolfram77
package org.event;

// required modules
import org.data.*;
import java.util.*;



public class EventEmitter {
    
    // data
    Map<String, Set<IEventListener>> listeners;
    
    
    // EventEmitter ()
    // - create an event emitter
    public EventEmitter() {
        listeners = new HashMap<>();
    }
    
    
    // Emit (e, event, args)
    // - emit a exception event to all listeners
    @SuppressWarnings("CallToPrintStackTrace")
    public void emit(Exception e, String event, Map args) {
        Set<IEventListener> lstn = listeners.get(event);
        if(lstn == null && e != null) { e.printStackTrace(); System.exit(-1); }
        if(lstn == null) return;
        for(IEventListener ev : lstn)
            ev.listen(event, args);
    }
    
    
    // Emit (e, event, args...)
    // - emit a exception event to all listeners
    public void emit(Exception e, String event, Object... args) {
        emit(e, event, Coll.map(args));
    }
    
    
    // Emit (e, event, args)
    // - emit a event to all listeners
    public void emit(String event, Map args) {
        emit(null, event, args);
    }
    
    
    // Emit (e, event, args...)
    // - emit a event to all listeners
    public void emit(String event, Object... args) {
        emit(event, Coll.map(args));
    }
    
    
    // Add (event, listener)
    // - add a listener to an event
    public void add(String event, IEventListener listener) {
        if(listeners.get(event) == null) listeners.put(event, new HashSet<IEventListener>());
        listeners.get(event).add(listener);
    }
    
    
    // Remove (event, listener)
    // - remove a listener from an event
    public void remove(String event, IEventListener listener) {
        Set<IEventListener> lstn = listeners.get(event);
        if(lstn != null) lstn.remove(listener);
    }
    
    
    // Remove (event, listener)
    // - remove all listener of an event
    public void remove(String event) {
        listeners.remove(event);
    }
    
    
    // Remove (event, listener)
    // - remove all event listeners
    public void remove() {
        listeners.clear();
    }
}
