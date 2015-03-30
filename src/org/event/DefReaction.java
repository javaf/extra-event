// @wolfram77
package org.event;

// required modules
import java.util.*;



/**
 * <b>Default reaction for stimulus with no reaction</b>
 * <div>Triggers on a stimulus with no reactions</div>
 * <div>(when fallback reaction is unchanged)</div>
 * @author wolfram77
 */
public class DefReaction implements Reactable {

    /**
     * <b>Print stimulus details and throw any error</b>
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    @Override
    public void on(String stimulus, Map args) {
        Reactable r = new DefReaction()::on;
        System.out.println("["+stimulus+"] : "+args);
        if(args.containsKey("err")) throw new SpineException((Throwable)args.get("err"));
    }
    
    public static void testStaticMethod(String stimulus, Map args) {
    }
}
