// @wolfram77
package org.event;

// required modules
import java.util.concurrent.*;
import java.util.*;



public class EventLoop extends Thread implements Reflexive {

    public Spine spine;
    BlockingQueue<Object[]> events;

    public EventLoop() {
    	spine = new Spine();
        spine.on("hello", EventLoop::onHello);
        spine.on("bye", this::onBye);
    	events = new LinkedBlockingQueue<>();
    }
    
    public static void onHello(String stimulus, Map args) {
    	System.out.println("Lets get to work");
    }
    
    public void onBye(String stimulus, Map args) {
        String name = "anonymous";
        System.out.println("Nice to meet you "+name);
    }
    
    @Override
    public void run() {
        try {
            while(true) {
                Object[] o = events.take();
                spine.is((String)o[0], (Map)o[1]);
            }
        }
        catch(InterruptedException e) {}
    }
    
    @Override
    public void on(String stimulus, Map args) {
        try { events.put(new Object[]{stimulus, args}); }
        catch(InterruptedException e) {}
    }
}
