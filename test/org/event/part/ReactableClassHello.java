package org.event.part;

// required modules
import java.util.*;
import org.event.Reflexive;



public class ReactableClassHello implements Reflexive {

    @Override
    public void on(String stimulus, Map args) {
        System.out.println("Lets get to work");
    }
}
