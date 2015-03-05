// @wolfram77
package org.event;



public class EventException extends Exception {

    // Exit (e)
    // - show stack trace and exit
    @SuppressWarnings("CallToPrintStackTrace")
    public static void exit(Throwable e) {
        e.printStackTrace();
        System.exit(-1);
    }
}
