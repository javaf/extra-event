// @wolfram77
package org.event;

// required modules
import java.util.*;



/**
 * <h3>Interface that can react on a stimulus</h3>
 * Create a class implementing this interface to be able to react to a stimulus
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
