package org.event;

// required modules
import java.util.*;



public class ReactableClassBye implements Reflexive {

    @Override
    public void on(String stimulus, Map args) {
        String name = "anonymous";
        System.out.println("Nice to meet you "+name);
    }
}
