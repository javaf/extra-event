package org.event;

// required modules
import java.util.*;



public class ReactableClassBye implements Reactable {

    @Override
    @Reacts("slow")
    public void on(String stimulus, Map args) {
        String name = "anonymous";
        System.out.println("Nice to meet you "+name);
    }
}
