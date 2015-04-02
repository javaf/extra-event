// @wolfram77
package org.event;

// required modules
import java.util.*;



/**
 * <b>Interface for reaction to stimulus</b>
 * <div>Classes implementing this interface can to react to stimuli</div>
 * @author wolfram77
 */
@FunctionalInterface
public interface Reflex {
    
    /**
     * <b>React on a stimulus</b>
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void on(String stimulus, Map args);
}
