// @wolfram77
package org.event;

// required modules
import java.util.*;



public interface IEventer {
    
    // Absorb (event, args)
    // - absorb an event, args provide additional info
    void absorb(String event, Map args);
}
