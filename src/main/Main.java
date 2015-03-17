// @wolfram77
package main;

// required modules
import org.event.*;

public class Main {

    public static void main(String[] args) {
        Spine spine = new Spine();
        try { throw new RuntimeException("Got a Sprain"); }
        catch(Exception e) { spine.is("injury", "err", e, "msg", "Cant go to school"); }
        // err argument indicates it is an error stimulus
    }
}
