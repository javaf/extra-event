// @wolfram77
package org.event;



public class EventException {

    
    // EventException (e)
    // - show stack trace and exit
    @SuppressWarnings("CallToPrintStackTrace")
    public EventException(Throwable e) {
        e.printStackTrace();
        System.exit(-1);
    }
}
