// @wolfram77
package org.event;

import java.util.*;



public class AsyncTest {
    
    public static void main(String[] args) {
        Spine spine = new Spine();
        System.out.println(Thread.currentThread());
        spine.on("hello-test", new Reflex(new HelloTestReflex()));
        spine.is("hello-test");
    }
}

class HelloTestReflex implements Reflexive {
    @Override
    @Speed("slow")
    public void on(String stimulus, Map args) {
        System.out.println(Thread.currentThread());
        System.out.println("Hello World");
        Scanner in = new Scanner(System.in);
        in.next();
    }
}

