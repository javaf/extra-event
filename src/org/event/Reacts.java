// @wolfram77
package org.event;

// required modules
import java.util.*;



/**
 * Describes an interface that can react on a stimulus
 * @author wolfram77
 */
public interface Reacts {
    
    /**
     * Reacts on a stimulus
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void on(String stimulus, Map args);
}
