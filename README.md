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
> processed by your spinal cord as a threat and an instant reflex was made to happen without your
> conscious thinking. However, the stimulus also triggered a slow (conscious) reflex to cool your
> burn in the hand with a piece of ice from the freezer.

`java-spine` is a minimal stimulus-reflex (event) library for Java. It provides a subscribe-publish
communication mechanism, and can be used in case of a event loop as well. Here, an event is considered
as a stimulus, and an event handler as the reaction. The `Spine` controls the activation of reactions
with respect to a stimulus. Also, note that a stimulus can have a specific location, and reflex is
triggered based on the location. This location is comparable to a `Spine` object.

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
first. That can help get a lot of things clear. I would suggest you try them out as well. To use this library, goto
[releases](https://github.com/wolfram77/java-spine/releases), download the JAR files and add them to your project.
Alternatiely download the source code as a Zip and copy the `src/org` folder in it and add it to your *project*. The 
[reference](https://github.com/wolfram77/java-spine/blob/master/README.md#reference) will help you know it all.

<br/>

## Examples

### Standard Stimulus
```java
[[Main.java]]
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
```
```
[hot-object] : {msg=Ouch!}
```

### Error Stimulus

```java
[[Main.java]]
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
```

```
[injury] : {msg=Cant go to school, err=java.lang.RuntimeException: Got a Sprain}
Exception in thread "main" org.event.SpineException: java.lang.RuntimeException: Got a Sprain
	at org.event.DefReaction.on(DefReaction.java:25)
	at org.event.Spine._is(Spine.java:85)
	at org.event.Spine.is(Spine.java:181)
	at org.event.Spine.is(Spine.java:195)
	at main.Main.main(Main.java:12)
Caused by: java.lang.RuntimeException: Got a Sprain
	at main.Main.main(Main.java:11)
Java Result: 1
```

### Reactable Class

```java
[[HelloReactor.java]]
package main;

// required modules
import java.util.*;
import org.event.*;

public class HelloReactor implements Reactable {

    @Override
    public void on(String stimulus, Map args) {
        System.out.println("Lets get to work");
    }
}


[[ByeReactor.java]]
package main;

// required modules
import java.util.*;
import org.event.*;

public class ByeReactor implements Reactable {

    @Override
    @Reacts("slow")
    public void on(String stimulus, Map args) {
        System.out.print("Name: ");
        Scanner in = new Scanner(System.in);
        String name = in.next();
        System.out.println("Nice to meet you "+name);
    }
}


[[Main.java]]
package main;

// required modules
import org.event.*;

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
```

```
Lets get to work
Name: anonymous
Nice to meet you anonymous
```

### Lambda & Anonymous Reactable

```java
[[Main.java]]
package main;

// required modules
import org.event.*;

public class Main {

    public static void main(String[] args) {
        // lambda expression is simpler
        Reactable helloReaction = (String stimulus, Map args) -> {
            System.out.println("Lets get to work");
        };
        // annotations allowed in anonymous class, but not in lambda expression
        Reaction byeReaction = new Reaction(new Reactable() {
            @Override
            @Reacts("slow")
            public void on(String stimulus, Map args) {
                String name = "anonymous";
                System.out.println("Nice to meet you "+name);
            }
        });
        Spine spine = new Spine();
        spine.on("hello", helloReaction).on("bye", byeReaction);
        spine.is("hello").is("bye");
    }
}
```

```
Lets get to work
Name: anonymous
Nice to meet you anonymous
```

### Reaction Method

```java
[[Main.java]]
package main;

// required modules
import java.util.*;
import org.event.*;

public class Main {
    
    public static void helloReactor(String stimulus, Map args) {
    	System.out.println("Lets get to work");
    }
    
    public void byeReactor(String stimulus, Map args) {
        System.out.print("Name: ");
        Scanner in = new Scanner(System.in);
        String name = in.next();
        System.out.println("Nice to meet you "+name);
    }
    
    public static void main(String[] args) {
    	Main main = new Main();
        Spine spine = new Spine();
        // static reaction method
        spine.on("hello", new Reaction(Main.class, "helloReactor"));
        // instance reaction method 
        // speed can be indicated manually as well
        spine.on("bye", new Reaction(main, "byeReactor").speed("slow"));
        spine.is("hello");
        spine.is("bye");
        // slow reactions trigger asynchronously
        System.out.println("ok?");
    }
}
```

```
Lets get to work
ok?
Name: anonymous
Nice to meet you anonymous
```

### Reaction Class

```java
[[Introducer.java]]
package main;

// required modules
import java.util.*;

public class Introducer {

    public static void onHello(String stimulus, Map args) {
        System.out.println("Lets get to work");
    }

    public void onBye(String stimulus, Map args) {
        System.out.print("Name: ");
        Scanner in = new Scanner(System.in);
        String name = in.next();
        System.out.println("Nice to meet you "+name);
    }
}


[[Main.java]]
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
```

```
Lets get to work
[bye] : {}

Lets get to work
Name: anonymous
Nice to meet you anonymous

Lets get to work
Lets get to work
Name: anonymous
Nice to meet you anonymous
```

### Event Loop

```java
[[Introducer.java]]
package main;

// required modules
import java.util.concurrent.*;
import java.util.*;
import org.event.*;

public class Introducer extends Thread implements Reactable {

    public Spine spine;
    BlockingQueue<Object[]> events;

    public Introducer() {
    	spine = new Spine(this);
    	events = new LinkedBlockingQueue<>();
    }
    
    public static void onHello(String stimulus, Map args) {
    	System.out.println("Lets get to work");
    }
    
    public void onBye(String stimulus, Map args) {
        System.out.print("Name: ");
        Scanner in = new Scanner(System.in);
        String name = in.next();
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


[[Main.java]]
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
```

```
Lets get to work
[bye] : {}

Lets get to work
Name: anonymous
Nice to meet you anonymous

Lets get to work
Lets get to work
Name: anonymous
Nice to meet you anonymous
```

<br/>

## Reference

### Spine

Represents stimuli with associated reactions <br/>
When a stimulus occurs, appropriate reactions are triggered

| **Spine** | `Map<String, Set<Reactable>>` |
|-----------|-------------------------------|
| **Spine** <br/>                                                                                                          `()`, `(cls)`, `(obj)` |                                                                                                     create spine : empty, with reactions from class, or object <br/>                                                             `Spine spine = new Spine();` <br/>                                                                                           `Spine stimuli = new Spine(NetHandler.class);` <br/>                                                                         `Spine stimuli = new Spine(netHandlerObj);` |
| **on** <br/>                                                                                                               `(stimulus, reaction)`, <br/> `(stimulus, reactions)`, <br/> `(stimuli, reaction)`, <br/> `(map)` |                          set reactions to trigger on stimuli <br/>                                                                          `spine.on("write-done", writeDoneReaction);` <br/>                                                                         `spine.on(multipleDone, doneReaction);` <br/>                                                                              `spine.on("done", doneReactions);` <br/>                                                                                   `spine.on(anotherSpine);` |
| **off** <br/>                                                                                                              `(stimulus, reaction)`, <br/> `(stimulus, reactions)`, <br/> `(stimuli, reaction)`, <br/> `(map)` |                           turn off reactions for stimuli <br/>                                                                             `spine.off("write-done", writeDoneReaction);` <br/>                                                                         `spine.off(multipleDone, doneReaction);` <br/>                                                                              `spine.off("done", doneReactions);` <br/>                                                                                   `spine.off(anotherSpine);` |
| **is** <br/>                                                                                                               `(event, args)` <br/> `(event, args...)` |                                                                                   indicate a stimulus, causing reactions to trigger <br/>                                                                      `spine.is("write", argsMap);` <br/>                                                                                          `spine.is("write", "err", e, "data", data);` |
| **speed** <br/>                                                                                                            `()`, `(speed)` |                                                                                                            get / set speed of spine <br/>                                                                                               `String speed = spine.speed();` <br/>                                                                                        `spine.speed("slow");` |
| *fallback* <br/> `()`, `(fallback)`                                                                                       | get / set fallback reaction, for stimulus with no reaction <br/>                                                           `Reactable fallback = spine.fallback();` <br/>                                                                             `spine.fallback(myFallbackReaction);` |

<br/>

### Reaction

Reaction to a stimulus <br/>
Encapsulates a reaction-method or `Reactable` object

| **Reaction** | `(Reactable)` |
|--------------|---------------|
| **Reaction** <br/>                                                                                                         `(reaction)`, `(reactable)`, <br/> `(cls, mthd)`, `(obj, mthd)` |                                                            create a reaction that can be invoked on a stimulus <br/>                                                                    `Reaction done = new Reaction(anotherReaction);` <br/>                                                                       `Reaction done = new Reaction(reactableObj);` <br/>                                                                          `Reaction done = new Reaction(MthdCls.class, "mthd");` <br/>                                                                 `Reaction done = new Reaction(MthdObj, "mthd");` |
| **speed** <br/>                                                                                                            `()`, `(speed)` |                                                                                                            get / set speed of reaction <br/>                                                                                            `String speed = reaction.speed();` <br/>                                                                                     `reaction.speed("slow");` |
| **on** <br/>                                                                                                               `(stimulus, args)` |                                                                                                         invoke reaction-method *(internal)* |

<br/>

### DefReaction

Default reaction for stimulus with no reaction <br/>
If a stimulus has no associated reactions, and the fallback reaction <br/>
is unchanged, then this reaction occurs

| **DefReaction** | `(Reactable)`  |
|-----------------|----------------|
| **on** <br/>                                                                                                               `(stimulus, args)` |                                                                                                         Print stimulus details and throw any error |

<br/>

### (Reactable)

Interface that can react on a stimulus <br/>
Create a class implementing this interface to be able to react to a stimulus

| **(Reactable)** |      |
|-----------------|------|
| **on** <br/>                                                                                                               `(stimulus, args)` |                                                                                                         react on a stimulus, can be attached to multiple stimulus |

<br/>

### @Reacts

Annotation to specify reaction-method speed *(internal)* <br/>
Use `@Reacts("fast")` for fast reaction-methods (optional) <br/>
Use `@Reacts("slow")` for slow reaction methods (required)

| **(@Reacts)** |     |
|---------------|-----|
| **value** <br/>                                                                                                            `()`, `(speed)` |                                                                                                            get / set speed of reaction-method |

<br/>

### SpineException

Unchecked exception for spine *(internal)*

| **SpineException** | `RuntimeException` |
|--------------------|--------------------|
| **SpineException** <br/>                                                                                                   `()`, `(msg)`, `(cause)` |                                                                                                   create spine exception - empty, with specified message or cause |
| **exit** <br/>                                                                                                             `()` |                                                                                                                       show stack trace and exit |
