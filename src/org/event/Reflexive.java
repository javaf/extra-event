// @wolfram77
package org.event;

// required modules
import java.util.*;



/**
 * Interface for response to stimulus. <p>
 * Classes implementing this interface can to respond to stimuli. </p>
 * @author wolfram77
 */
public interface Reflexive {
    
    /**
     * Respond to a stimulus.
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void on(String stimulus, Map args);
}
