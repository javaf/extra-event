// @wolfram77
package main;

// required modules
import org.event.*;

public class Main {

    public static void main(String[] args) {
        Spine spine = new Spine();
        // trigger default reaction
        spine.is("hot-object", "msg", "Ouch!");
    }
}
