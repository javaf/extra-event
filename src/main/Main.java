// @wolfram77
package main;

// required modules
import org.event.*;



public class Main {
    
    public static void main(String[] args) {
        EventEmitter event = new EventEmitter();
        event.emit("hello", "msg", "Hello World!");
    }
}
