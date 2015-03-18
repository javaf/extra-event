// @wolfram77
package org.event;

// required modules
import java.util.*;



public class ReactionClass {

    public static void onHello(String stimulus, Map args) {
        System.out.println("Lets get to work");
    }

    public void onBye(String stimulus, Map args) {
        String name = "anonymous";
        System.out.println("Nice to meet you "+name);
    }
}
