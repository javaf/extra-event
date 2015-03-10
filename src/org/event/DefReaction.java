// @wolfram77
package org.event;

// required modules
import java.util.*;



/**
 * Default reaction for stimulus with no other specified reaction
 * @author wolfram77
 */
public class DefReaction implements Reactable {

    /**
     * Print stimulus details and exit on error
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    @Override
    public void on(String stimulus, Map args) {
        System.out.println("["+stimulus+"] : "+args);
        if(!args.containsKey("err")) return;
        new StimuliException((Throwable)args.get("err")).exit();
    }
}
