// @wolfram77
package main;

// required modules
import java.util.*;
import org.event.*;

public class Main {

    public static void helloReactor(String stimulus, Map args) {
        System.out.println("Lets get to work");
    }

    public void byeReactor(String stimulus, Map args) {
        System.out.print("Name: ");
        Scanner in = new Scanner(System.in);
        String name = in.next();
        System.out.println("Nice to meet you "+name);
    }

    public static void main(String[] args) {
        Main main = new Main();
        Spine spine = new Spine();
        // static reaction method
        spine.on("hello", new Reaction(Main.class, "helloReactor"));
        // instance reaction method 
        // speed can be indicated manually as well
        spine.on("bye", new Reaction(main, "byeReactor").speed("slow"));
        spine.is("hello");
        spine.is("bye");
    }
}