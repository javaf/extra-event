// @wolfram77
package org.event;



public class EventException extends Exception {

    // EventException ()
    // - create event exception
    public EventException() {
    }
    
    
    // EventException (e)
    // - create event exception with given cause
    public EventException(Throwable e) {
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
