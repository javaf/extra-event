package org.event;

// required modules
import java.util.*;



public class ReactionMethods {

    public static void helloReactor(String stimulus, Map args) {
        System.out.println("Lets get to work");
    }

    public void byeReactor(String stimulus, Map args) {
        String name = "anonymous";
        System.out.println("Nice to meet you "+name);
    }
}
