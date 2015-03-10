// @wolfram77
package org.event;



/**
 * Stimuli specific unchecked exception
 * @author wolfram77
 */
public class StimuliException extends RuntimeException {

    /**
     * Create stimuli exception
     */
    public StimuliException() {
    }
    
    
    /**
     * Create stimuli exception with given cause
     * @param cause cause of exception
     */
    public StimuliException(Throwable cause) {
        super(cause);
    }
    
    
    /**
     * Show stack trace and exit
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public void exit() {
        printStackTrace();
        System.exit(-1);
    }
}
