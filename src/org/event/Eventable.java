// @wolfram77
package org.event;



/**
 * Classes implementing this interface can accept reflexes to stimuli. <p>
 * In other words, it can be used to notify events to listeners. </p>
 * @author wolfram77
 */
public interface Eventable {
    
    /**
     * Returns the spine object for events associated with this object.
     * @return spine
     */
    Spine event();
}
