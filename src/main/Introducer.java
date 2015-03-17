// package
package main;

// required modules
import java.util.*;
import org.event.*;

public class Introducer {

    public static void onHello(String stimulus, Map args) {
        System.out.println("Lets get to work");
    }

    @Reacts("slow")
    public void onBye(String stimulus, Map args) {
        System.out.print("Name: ");
        Scanner in = new Scanner(System.in);
        String name = in.next();
        System.out.println("Nice to meet you "+name);
    }
}
