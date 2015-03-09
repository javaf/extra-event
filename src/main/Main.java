// @wolfram77
package main;

// required modules
import java.util.*;
import org.event.*;



public class Main {
    
    public static void onMouseClick(String event, Map args) {
    }
    
    public void onKeyPress(String event, Map args) {
    }
    
    public static void main(String[] args) {
        int N = 1000000;
        EventEmitter event = new EventEmitter(Main.class);
        System.out.println(event);
        long t0 = System.nanoTime();
        for(int i=0; i<N; i++)
            event.emit("mouse-click");
        long t1 = System.nanoTime();
        for(int i=0; i<N; i++)
            onMouseClick(null, null);
        long t2 = System.nanoTime();
        double tmh = (t1-t0)/Math.pow(10, 9);
        double ti = (t2-t1)/Math.pow(10, 9);
        System.out.println("MH = "+tmh+", I = "+ti);
    }
}
