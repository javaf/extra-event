// @wolfram77
package main;

// required modules
import org.event.*;

public class Main {

    public static void main(String[] args) {
        Introducer introducer = new Introducer();
        // only static reaction methods are triggered
        Spine spine1 = new Spine(Introducer.class);
        spine1.is("hello").is("bye");
        System.out.println();
        // both static and instance methods are triggered
        Spine spine2 = new Spine(introducer);
        spine2.is("hello").is("bye");
        System.out.println();
        // import spine1 to spine2 (or any Map<String, Reactable>)
        spine2.on(spine1);
        spine2.is("hello").is("bye");
    }
}
