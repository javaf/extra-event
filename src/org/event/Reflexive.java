// @wolfram77
package org.event;

// required modules
import java.util.*;



/**
 * <b>Interface for response to stimulus</b>
 * <div>Classes implementing this interface can to respond to stimuli</div>
 * @author wolfram77
 */
@FunctionalInterface
public interface Reflexive {
    
    /**
     * <b>Respond to a stimulus</b>
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void on(String stimulus, Map args);
}
