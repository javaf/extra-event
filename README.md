# java-spine

What is `java-spine` you ask? Here is a short-story to help understand.

> Today is Diwali. You waited a long time for this, didn't you? The dark night is lit up with
> numerous lights. People have gathered around showers of sparkles oozing from the ground. In
> the far reaches of the world, beyond your sight, you can hear chained explosions being triggered
> at random intervals. Darkness is neither spared in the dome skies, as sprays of light spin
> around with joy.

> Ah! Your crackers are cracked to dryness, ready for their annhilation. But, you are a new
> kid to this business, and this is the first time you are taking charge. Your mother just
> laid down the last of deeps' (tear-drop shaped clay candles). Next minute, a sparkler is
> in your hand. You make sure no one is watching, because you want to do it on your own.
> After all, you want to treated like a big boy in your house.

> Flame of a deep heats the sparkler. Nothing. You wait. The wait seems for ever, but it never
> starts to sparkle, but then... Ouch! A pulse rushes down your spine, and without a thought
> the ignited sparkler goes flying off your hand. Now you are in your conscious mode, and start
> to think what just happened. Then you realize you should cool it off with a block of ice,
> or else you might have a bad burn on your hand, and you just lost your best day of this year.

> Okay, now think of this. The sparkler which suddenly ignited was a stimulus that was immediately
> processed by your spinal cord as a threat and an instant reaction was made to happen without your
> conscious thinking. However, the stimulus also triggered a slow (conscious) response to cool your
> burn in the hand with a piece of ice from the freezer.

`java-spine` is a minimal stimulus-response (event) library for Java. It provides a subscribe-publish
communication mechanism, and can be used in case of a event loop as well. Here, an event is considered
as a stimulus, and an event handler as the reaction. The `Spine` controls the activation of reactions
with respect to a stimulus.

Here, reactions are categorized as fast or slow. Fast reactions are executed sychronously (because they
are fast), and slow reactions are executed asynchrounously. In the exact same way, a spine is categorized
as fast or slow. Fast spine invokes all its reactions synchronously (because it is fast), and a slow spine
invokes all its reactions asynchronously. Fast or slow indicate the speed of something.

Reactions can be objects of classes that implement the `Reactable` interface, or independent static / instance
methods of any class. An annotations `@Reacts("slow")` can be used to indicate slow reaction methods, and
`@Reacts("fast")` can be used to indicate fast reaction methods (this one is optional). It is also possible
to create a spine from an entire class, in which case functions names as `on<stimulus>` will be registered
as reactions for `<stimulus>` stimulus. Again, annotaations can be used to indicate the speed of each reaction
method. It is also possible to inherit stimulus-reaction associations from another spine.

<br/>

## Usage

You can get started by going through the [examples](https://github.com/wolfram77/java-spine/blob/master/README.md#examples)
first. That can help get a lot of things clear. I would suggest you try them out as well. To use this library,
download the source code as a Zip and copy the `src/org` folder in it and add it to your *project*. The  [reference](https://github.com/wolfram77/java-spine/blob/master/README.md#reference) will help you know it all.

<br/>

## Examples

### Ouch!
```java
// required modules
import org.event.*;

public class Main {
    
    public static void main(String[] args) {
        Spine spine = new Spine();
        spine.is("hot-object", "msg", "Ouch!"); // trigger default reaction
    }
}
```
```
[hot-object] : {msg=Ouch!}
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
