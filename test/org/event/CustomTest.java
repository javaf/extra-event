// @wolfram77
package org.event;

import java.util.*;



public class CustomTest {
    
    public static void main(String[] args) {
        Spine spine = new Spine();
        System.out.println(Thread.currentThread());
        spine.on("hello", new Reflex(new HelloReflex()));
        spine.is("hello");
    }
}

class HelloReflex implements Reflexive {
    @Override
    @Speed("slow")
    public void on(String stimulus, Map args) {
        System.out.println(Thread.currentThread());
        System.out.println("Hello World");
        Scanner in = new Scanner(System.in);
        in.next();
    }
}

