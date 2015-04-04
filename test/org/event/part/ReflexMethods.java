// @wolfram77
package org.event.part;

// required modules
import java.util.*;



public class ReflexMethods {

    public static void onHello(String stimulus, Map args) {
        System.out.println("ReflexMethod: Lets get to work");
    }

    public void onBye(String stimulus, Map args) {
        String name = "anonymous";
        System.out.println("ReflexMethod: Nice to meet you "+name);
    }
}
