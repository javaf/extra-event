// @wolfram77
package org.event;

// required modules
import java.util.*;



/**
 * <h3>Default reaction for stimulus with no reaction</h3>
 * <div>Triggers on a stimulus with no reactions</div>
 * <div>(when fallback reaction is unchanged)</div>
 * @author wolfram77
 */
public class DefReaction implements Reactable {

    /**
     * <h3>Print stimulus details and throw any error</h3>
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    @Override
    public void on(String stimulus, Map args) {
        System.out.println("["+stimulus+"] : "+args);
        if(args.containsKey("err")) throw new SpineException((Throwable)args.get("err"));
    }
}
