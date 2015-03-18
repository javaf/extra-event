package org.event;

// required modules
import java.util.*;



public class ReactableClassHello implements Reactable {

    @Override
    public void on(String stimulus, Map args) {
        System.out.println("Lets get to work");
    }
}
