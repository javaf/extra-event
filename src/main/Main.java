// @wolfram77
package main;

// required modules
import java.util.*;
import org.event.*;

class HelloReactor implements Reactable {

    @Override
    public void on(String stimulus, Map args) {
        System.out.println("Lets get to work");
    }
}

class ByeReactor implements Reactable {

    @Override
    @Reacts("slow")
    public void on(String stimulus, Map args) {
        System.out.print("Name: ");
        Scanner in = new Scanner(System.in);
        String name = in.next();
        System.out.println("Nice to meet you "+name);
    }
}

public class Main {

    public static void main(String[] args) {
        HelloReactor helloReaction = new HelloReactor();
        // annotations only work in Reaction objects
        Reaction byeReaction = new Reaction(new ByeReactor());
        Spine spine = new Spine();
        // chaining method calls is supported
        spine.on("hello", helloReaction).on("bye", byeReaction);
        spine.is("hello").is("bye");
    }
}
