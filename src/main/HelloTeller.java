// @wolfram77
package main;

// required modules
import java.util.*;
import org.event.*;



public class HelloTeller implements IEventListener {
    
    @Override
    public void listen(String event, Map args) {
        System.out.println("Hello event "+event);
    }
}
