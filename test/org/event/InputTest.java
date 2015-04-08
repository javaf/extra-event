// @wolfram77
package org.event;

// required modules
import java.util.*;



public class InputTest {
    
    public static void onHello(String stimulus, Map args) {
        System.out.println("Hello");
    }
    
    @Speed("slow")
    public static void onBye(String stimulus, Map args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Name: ");
        String name = in.next();
        System.out.println("Bye "+name);
    }
    
    public static void main(String[] args) {
        Spine spine = new Spine();
        spine.on("hello", new Reflex(InputTest.class, "onHello"));
        spine.on("bye", new Reflex(InputTest.class, "onBye"));
        spine.is("hello").is("bye");
    }
}
