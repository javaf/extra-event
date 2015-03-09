// @wolfram77
package main;

// required modules
import java.util.*;
import org.event.*;



public class Main {
    
    public static void onMouseClick(String event, Map args) {
        System.out.println("mouse clicked");
    }
    
    public void onKeyPress(String event, Map args) {
    }
    
    public static void main(String[] args) {
        EventEmitter event = new EventEmitter();
        event.on(Main.class);
        System.out.println(event);
        event.emit("mouse-click");
    }
}