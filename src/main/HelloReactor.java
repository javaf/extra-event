// @wolfram77
package main;

// required modules
import java.util.*;
import org.event.*;



public class HelloReactor implements Reactable {

    @Override
    public void on(String stimulus, Map args) {
        System.out.println("Lets get to work");
    }
}
