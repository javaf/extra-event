// @wolfram77
package com.event;



public class EventException extends Exception {
    
    // EventException ()
    // - creates an event exception
    public EventException() {
    }
    
    
    // EventException ()
    // - creates an event exception with a cause
    public EventException(Throwable e) {
        super(e);
    }
    
    
    // Exit ()
    // - display stack trace and exit
    @SuppressWarnings("CallToPrintStackTrace")
    public void exit() {
        this.printStackTrace();
        System.exit(-1);
    }
}
