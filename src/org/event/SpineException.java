// @wolfram77
package org.event;



/**
 * <b>Unchecked exception for {@linkplain Spine}</b>
 * @author wolfram77
 */
public class SpineException extends RuntimeException {

    /**
     * <b>Create an empty spine exception</b>
     */
    public SpineException() {
    }

    
    /**
     * <b>Create a spine exception with specified message</b>
     * @param msg exception message
     */
    public SpineException(String msg) {
        super(msg);
    }
    
    
    /**
     * <b>Create a spine exception with specified cause</b>
     * @param cause exception cause
     */
    public SpineException(Throwable cause) {
        super(cause);
    }
    
    
    /**
     * <b>Show stack trace and exit</b>
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public void exit() {
        printStackTrace();
        System.exit(-1);
    }
}
