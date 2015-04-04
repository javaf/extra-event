package org.event.part;

// required modules
import java.util.*;
import org.event.*;



public class ByeReflex implements Reflexive {

    @Override
    @Speed("slow")
    public void on(String stimulus, Map args) {
        String name = "anonymous";
        System.out.println("Reflexive: Nice to meet you "+name);
    }
}
