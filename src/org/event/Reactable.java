// @wolfram77
package org.event;

// required modules
import java.util.*;



/**
 * <b>Interface that can react on a stimulus</b>
 * <div>Classes implementing this interface are able to react to stimuli</div>
 * @author wolfram77
 */
public interface Reactable {
    
    /**
     * <b>React on a stimulus</b>
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void on(String stimulus, Map args);
}
