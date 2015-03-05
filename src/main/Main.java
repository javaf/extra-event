// @wolfram77
package main;

// required modules
import java.util.*;
import org.event.*;



public class Main {
    
    public void helloAbsorb(String event, Map args) {
        System.out.println(event+" = "+args);
    }
    
    public static void main(String[] args) throws Throwable {
        EventEmitter event = new EventEmitter();
        event.add("hello", new EventAbsorber(new Main(), "helloAbsorb"));
        event.emit("hello", "item", "world");
    }
}
