// @wolfram77
package org.event;

// required modules
import java.util.*;



/**
 * <h3>Interface that can react on a stimulus</h3>
 * <div>Classes implementing this interface are able to react to stimuli</div>
 * @author wolfram77
 */
public interface Reactable {
    
    /**
     * <h3>React on a stimulus</h3>
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void on(String stimulus, Map args);
}
