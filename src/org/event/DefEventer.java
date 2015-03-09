// @wolfram77
package org.event;

// required modules
import java.util.*;



public class DefEventer implements Eventable {

    // Absorb (event, args)
    // - print event details and exit on error
    @Override
    public void absorb(String event, Map args) {
        System.out.println("["+event+"] : "+args);
        if(!args.containsKey("err")) return;
        new EventException((Throwable)args.get("err")).exit();
    }
}
