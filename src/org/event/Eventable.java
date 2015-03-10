// @wolfram77
package org.event;

// required modules
import java.util.*;



public interface Eventable {
    
    // On (event, args)
    // - listen to an event, args provide additional info
    void on(String event, Map args);
}
