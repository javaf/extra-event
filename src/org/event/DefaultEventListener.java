// @wolfram77
package org.event;

// required modules
import java.util.*;



public class DefaultEventListener implements IEventListener {

    @Override
    public void listen(String event, Map args) {
        System.out.println("["+event+"] : "+args);
    }
}
