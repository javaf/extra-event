// @wolfram77
package org.event.part;

// required modules
import java.util.concurrent.*;
import java.util.*;
import org.event.*;



public class EventLoop extends Thread implements Reflexive {

    public Spine spine;
    BlockingQueue<Object[]> events;

    public EventLoop() {
    	spine = new Spine(this);
    	events = new LinkedBlockingQueue<>();
    }
    
    public void onHello(String stimulus, Map args) {
    	System.out.println("EventLoop: Lets get to work");
    }
    
    @Speed("slow")
    public void onBye(String stimulus, Map args) {
        String name = "anonymous";
        System.out.println("EventLoop: Nice to meet you "+name);
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
