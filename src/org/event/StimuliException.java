// @wolfram77
package org.event;



/**
 * <h3>Unchecked exception for {@linkplain Stimuli}</h3>
 * @author wolfram77
 */
public class StimuliException extends RuntimeException {

    /**
     * <h3>Create an empty {@linkplain StimuliException}</h3>
     */
    public StimuliException() {
    }

    
    /**
     * <h3>Create a {@linkplain StimuliException} with specified message</h3>
     * @param msg expection message
     */
    public StimuliException(String msg) {
        super(msg);
    }
    
    
    /**
     * <h3>Create a {@linkplain StimuliException} with specified cause</h3>
     * @param cause exception cause
     */
    public StimuliException(Throwable cause) {
        super(cause);
    }
    
    
    /**
     * <h3>Show stack trace and exit</h3>
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public void exit() {
        printStackTrace();
        System.exit(-1);
    }
}
