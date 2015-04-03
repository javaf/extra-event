package org.event.part;

// required modules
import java.util.*;
import org.event.Reflexive;



public class ByeReflex implements Reflexive {

    @Override
    public void on(String stimulus, Map args) {
        String name = "anonymous";
        System.out.println("Nice to meet you "+name);
    }
}
