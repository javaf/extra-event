// @wolfram77
package org.event;

// required modules
import java.util.*;



/**
 * Interface that can reacted on a stimulus
 * @author wolfram77
 */
public interface Reactable {
    
    /**
     * Reacts on a stimulus
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void on(String stimulus, Map args);
}
