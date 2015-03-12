// @wolfram77
package org.event;



/**
 * <h3>Unchecked exception for {@linkplain Spine}</h3>
 * @author wolfram77
 */
public class SpineException extends RuntimeException {

    /**
     * <h3>Create an empty spine exception</h3>
     */
    public SpineException() {
    }

    
    /**
     * <h3>Create a spine exception with specified message</h3>
     * @param msg expection message
     */
    public SpineException(String msg) {
        super(msg);
    }
    
    
    /**
     * <h3>Create a spine exception with specified cause</h3>
     * @param cause exception cause
     */
    public SpineException(Throwable cause) {
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
