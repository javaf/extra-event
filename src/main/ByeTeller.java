// @wolfram77
package main;

// required modules
import java.util.*;
import org.event.*;



public class ByeTeller implements IEventListener {
    
    @Override
    public void listen(String event, Map args) {
        System.out.println("Bye event "+event);
    }
}
