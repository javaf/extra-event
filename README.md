# java-spine

What is `java-spine` you ask? Let us understand it.

> Today is Diwali. You waited a long time for this, didn't you? The dark night is lit up with
> numerous lights. People have gathered around showers of sparkles oozing from the ground. In
> the far reaches of the world, beyond your sight, you can hear chained explosions being triggered
> at random intervals. Darkness is neither spared in the dome skies, as sprays of light spin
> around with joy.

> Ah! Your crackers are cracked to dryness, ready for their annhilation. But, you are a new
> kid to this business, and this is the first time you are taking charge. Your mother just
> laid down the last of deeps' (tear-drop shaped clay candles). Next minute, a sparkler is
> in your hand. You make sure no one is watchine, because you want to do it on your own.
> After all, you want to treated like a big boy in your house.

> Flame of a deep heats the sparkler. Nothing. You wait. The wait seems for ever, but it never
> starts to sparkle, but then... Ouch! A pulse rushes down your spine, and without a thought
> the ignited sparkler goes flying off your hand. Now you are in your conscious mode, and start
> to think what just happened. Then you realize you should cool it off with a block of ice,
> or else you might have a bad burn on your hand, and you just lost your best day of this year.


Okay, that was bad. But atleast you now know that we use events in our life. In fact we use it
all the time. If you have ever heard of *interrupts*, they are essentially *hardware triggered events*,
which provide a great solution to monitoring something without repeated polling. *Software events*
are similar, except that the triggering of the event is performed by software as well.

`Emitting` an event is similar to *making a function call*, and `absorbing` is the act of
*getting a function call*. How is it different from a function call then? Well, with the idea
of events, there can be any number of *event absorbers*, and as many *types of events* as you like,
and they can be added and removed at runtime. A more *dynamic form of function call*.

<br/>

## Usage

Shall we get started?  Go through the [examples](https://github.com/wolfram77/java-event-emitter#examples),
download the source code `src/org/data` and `src/org/event`, add it to your *project* and follow the
[reference](https://github.com/wolfram77/java-event-emitter#reference).

<br/>

## Examples

### Hello World!
```java
// required modules
import org.event.*;

public class Main {
    
    public static void main(String[] args) {
        EventEmitter event = new EventEmitter();    // create event emitter
        event.emit("hello", "msg", "Hello World!"); // DefEventer listens
    }
}
```
```
[hello] : {msg=Hello World!}
```

### Error Event

```java
// required modules
import org.event.*;

public class Main {
    
    public static void main(String[] args) {
        EventEmitter event = new EventEmitter();
        try { int a = 1 / 0; }
        catch(Exception e) { event.emit("hello", "err", e, "msg", "Hello World!"); }
        // err argument indicates it is an error event
    }
}
```

```
[hello] : {msg=Hello World!, err=java.lang.ArithmeticException: / by zero}
java.lang.ArithmeticException: / by zero
	at main.Main.main(Main.java:13)
Java Result: -1
```

### Event Absorber Class

```java
// required modules
import java.util.*;
import org.event.*;

class HelloTeller implements Eventable {
    
    @Override
    public void absorb(String event, Map args) {
        System.out.println("Hello event "+event);
    }
}

class ByeTeller implements Eventable {
    
    @Override
    public void absorb(String event, Map args) {
        System.out.println("Bye event "+event);
    }
}

public class Main {
    
    public static void main(String[] args) {
        HelloTeller hello = new HelloTeller();
        ByeTeller bye = new ByteTeller();
        EventEmitter event = new EventEmitter();
        event.add("action", hello).add("action", bye);
        event.emit("action");
    }
}
```

```
Hello event action
Bye event action
```

### Event Absorber Method

```java
// required modules
import java.util.*;
import org.event.*;

public class Main {
    
    public static void helloTeller(String event, Map args) {
    	System.out.println("Hello event "+event);
    }
    
    public void byeTeller(String event, Map args) {
    	System.out.println("Bye event "+event);
    }
    
    public static void main(String[] args) {
    	Main main = new Main();
        EventEmitter event = new EventEmitter();
        event.add("action", new Eventer(Main.class, "helloTeller"));
        event.add("action", new Eventer(main, "byeTeller");
        event.emit("action");
    }
}
```

```
Hello event action
Bye event action
```

<br/>

## Reference

| **Spine** | `Map<String, Set<Reactable>>` |
|-----------|-------------------------------|
| **Spine** <br/>                                                                                                          `()`, `(cls)`, `(obj)` |                                                                                                     create multiple stimulus with associated reactions <br/>                                                                     it can be empty, with reactions from class, or from an object <br/>                                                          `Spine spine = new Spine();` <br/>                                                                                     `Spine stimuli = new Spine(NetHandler.class);` <br/>                                                                     `Spine stimuli = new Spine(netHandlerObj);` |
| **on** <br/>                                                                                                               `(stimulus, reaction)`, <br/> `(stimulus, reactions)`, <br/> `(stimuli, reaction)`, <br/> `(map)` |                          set reactions to trigger on multiple stimulus <br/>                                                                          `spine.on("write-done", writeDoneReaction);` <br/>                                                                         `spine.on(multipleDone, doneReaction);` <br/>                                                                              `spine.on("done", doneReactions);` <br/>                                                                                   `spine.on(anotherStimuli);` |
| **off** <br/>                                                                                                              `(stimulus, reaction)`, <br/> `(stimulus, reactions)`, <br/> `(stimuli, reaction)`, <br/> `(map)` |                           turn off reactions for multiple stimulus <br/>                                                                             `spine.off("write", writeDoneEventer);` <br/>                                                                              `spine.off("write");` |
| **is** <br/>                                                                                                               `(event, args)` <br/> `(event, args...)` |                                                                                   indicate a stimulus, causing reactions to trigger <br/>                                                                      `spine.is("write", "time", new Date(), "data", data);` <br/>                                                             `spine.is("write", "err", e, "data", data);` |
| *fallback* <br/> `()`, `(fallback)`                                                                                       | get / set fallback reaction (for stimulus with no reactions) <br/>                                                           `Reactable fallback = spine.fallback();` <br/>                                                                             `spine.fallback(myFallbackReaction);` |

<br/>

| **(Reactable)** |                        |
|-----------------|------------------------|
| **on** <br/>                                                                                                               `(stimulus, args)` |                                                                                                         called when object implementing this interface is set as reaction <br/>                                                      one reaction can be attached to multiple stimulus, hence *stimulus* |

<br/>

| **Reaction** | `(Reactable)`  |
|--------------|----------------|
| **Reaction** <br/>                                                                                                         `(reaction)`, `(reactable)`, `(cls, mthd)`, <br/> `(obj, mthd)` |                                                            create a reaction from another reaction, reactable object, or static / instance method <br/>                                 `Reaction done = new Reaction(MthdCls.class, "mthd");` <br/>                                                                 `Reaction done = new Reaction(MthdObj, "mthd");` |

<br/>

| **DefReaction** | `(Reactable)`  |
|-----------------|----------------|
| **on** <br/>                                                                                                               `(stimulus, args)` |                                                                                                         reacts on stimulus with no reactions |
