// @wolfram77
package org.event;

// required modules
import java.util.*;



/**
 * <h3>Interface that can be reacted on a stimulus</h3>
 * @author wolfram77
 */
public interface Reactable {
    
    /**
     * <h3>Reacts on a stimulus</h3>
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void on(String stimulus, Map args);
}
