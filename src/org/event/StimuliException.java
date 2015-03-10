// @wolfram77
package org.event;



public class StimuliException extends RuntimeException {

    // StimuliException ()
    // - create event exception
    public StimuliException() {
    }
    
    
    // StimuliException (e)
    // - create event exception with given cause
    public StimuliException(Throwable e) {
        super(e);
    }
    
    
    // Exit (e)
    // - show stack trace and exit
    @SuppressWarnings("CallToPrintStackTrace")
    public void exit() {
        printStackTrace();
        System.exit(-1);
    }
}
