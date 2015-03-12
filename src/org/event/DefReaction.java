// @wolfram77
package org.event;

// required modules
import java.util.*;



/**
 * <h3>Default reaction for stimulus with no reaction</h3>
 * If a stimulus has no associated reactions, and the fallback reaction
 * is unchanged, then this reaction occurs
 * @author wolfram77
 */
public class DefReaction implements Reactable {

    /**
     * <h3>Print stimulus details and exit if error</h3>
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    @Override
    public void on(String stimulus, Map args) {
        System.out.println("["+stimulus+"] : "+args);
        if(!args.containsKey("err")) return;
        new SpineException((Throwable)args.get("err")).exit();
    }
}
