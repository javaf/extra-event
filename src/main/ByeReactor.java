// @wolfram77
package main;

// required modules
import java.util.*;
import org.event.*;



public class ByeReactor implements Reactable {

    @Override
    @Reacts("slow")
    public void on(String stimulus, Map args) {
        System.out.print("Name: ");
        Scanner in = new Scanner(System.in);
        String name = in.next();
        System.out.println("Nice to meet you "+name);
    }
}
