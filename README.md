# java-spine <img src="https://travis-ci.org/wolfram77/java-spine.svg?branch=master">

`java-spine` is a *minimal* **stimulus-reflex** *(event)* library for *Java*. It is based on
[publish-subscribe pattern](http://en.wikipedia.org/wiki/Publish%E2%80%93subscribe_pattern),
and can be used in [event-loop construct](http://en.wikipedia.org/wiki/Event_loop) as well, see
[examples](#examples). Here, everthing related to **events** in computer science is named in
terms of their biological counterparts to make it easier to **feel** and *memorize*.

It is [simple](#reference) and [tiny](https://github.com/wolfram77/java-spine/releases),
helping you reduce your design complexity, development time and software bugs. `java-spine`
is available for *Java 6 and above*, and can be used for **Desktop**, **Web**, or **Mobile**
*(Android)*.

<img src="/assets/img/example.png" width="100%"><br/>

<br/>

## Concept

[Spinal Cord](http://en.wikipedia.org/wiki/Spinal_cord) or **Spine** is the connected set of
thinking bones in our body. We know it as the one who pulls our hand back from a *hot object*,
or leg from a *sharp stone*. It autonomously and almost instantly provides a *reflex*
to a *stimulus* and also informs our brain to enable it to do something more on a given
stimulus. `java-spine` follows this *concept*.

`java-spine` consists of *class* [Spine](#spine) which is used to **indicate** *reflexes* to
*stimuli* as `spine.on(stimuli, reflexes)`. Appropriate *reflexes* are **triggered** when a
*stimulus* is *indicated* as `spine.is(stimulus, args)`. Any *reflex* to a *stimulus* can
be removed as `spine.off(stimuli, reflexes)`. If a *stimulus* has *no associated reflex*, the
**fallback** reflex is invoked, whch simply *prints out* the *details* of the *stimulus*.
This behaviour can be changed with `Spine.fallback(reflex)` to your *own fallback reflex*.

A **reflex** can be made as a [class implementing](#implement-reflexive) *interface*
[Reflexive](#reflexive), as [anonymous class](#anonymous-class), or as [methods encapsulated](#reflex-method)
by *class* [Reflex](#reflex). *Reflexes* are categorized as **slow** and **fast**. A *fast reflex*
is invoked by `Spine` *synchronously* (because it is *fast*), while a *slow reflex asynchronously*
(on a seperate *thread* from *threadpool*). This *speed* can be set by using *annotation*
[@Speed](#speed) as `@Speed("slow")` for *slow reflex* (`@Speed("fast")` is *optional*). It can also
be set by `reflex.speed(speed)` (*not suggested*).

While *indicating* a *stimulus* as `spine.is("mouse-click", "x", x, "y", y)`, *additional arguments*
or information can be passed to the *reflex*. This information can be *obtained* at the *reflex* as
`args.get("x")` to get `x` *value* passed above. It is possible to create an class full of reflexes
which respond to specific stimulus by creating **static** methods with names as `on<stimulus>` and
creating a `Spine` object as `new Spine(ClassName.class)`. A similar way is possible for **instance**
methods. This technique can be used to [create an Event Loop](#event-loop).

<br/>

## Usage

Get started by going through the [examples](#examples). To use this library, goto
[releases](https://github.com/wolfram77/java-spine/releases), and download the *JAR files*,
`java-spine.jar`, `java-spine-sources.jar`, `java-spine-javadoc.jar` of the appropriate
*JDK version* and add them to your *project*. Use [reference](#reference) or
[javadoc](http://wolfram77.github.io/java-spine/docs/jdk6/index.html) while developing
an *application*.

<br/>

## Examples

### Default Reflex

> Main.java

```java
package main;

// required modules
import org.event.*;

public class Main {
    
    public static void main(String[] args) {
        Spine spine = new Spine();
        // trigger default reflex
        spine.is("hot-object", "msg", "Ouch!");
    }
}
```

> Output : [@Runnable](http://runnable.com/VSUrcKxatQpHThx9/default-reflex-for-java)

```
[hot-object] : {msg=Ouch!}
```


### Implement Reflexive

> HelloReflex.java

```java
package main;

// required modules
import java.util.*;
import org.event.*;

public class HelloReflex implements Reflexive {

    @Override
    public void on(String stimulus, Map args) {
        System.out.println("Lets get to work");
    }
}
```

> ByeReflex.java

```java
package main;

// required modules
import java.util.*;
import org.event.*;

public class ByeReflex implements Reflexive {

    @Override
    @Speed("slow")
    public void on(String stimulus, Map args) {
        System.out.print("Name: ");
        Scanner in = new Scanner(System.in);
        String name = in.next();
        System.out.println("Nice to meet you "+name);
    }
}
```

> Main.java

```java
package main;

// required modules
import org.event.*;

public class Main {

    public static void main(String[] args) {
        HelloReflex hello = new HelloReflex();
        // annotations only work when encapsulated by Reflex
        Reflexive bye = new Reflex(new ByeReflex());
        Spine spine = new Spine();
        // chaining method calls is supported
        spine.on("hello", hello).on("bye", bye);
        spine.is("hello").is("bye");
    }
}
```

> Output : [@Runnable](http://runnable.com/VSUh3Ym0HHpG68cB/implement-reflexive-for-java)

```
Lets get to work
Name: anonymous
Nice to meet you anonymous
```

### Anonymous Class

> Main.java

```java
package main;

// required modules
import java.util.*;
import org.event.*;

public class Main {

    public static void main(String[] args) {
        // annotations allowed in anonymous class
        Reflexive bye = new Reflex(new Reflexive() {
            @Override
            @Speed("slow")
            public void on(String stimulus, Map args) {
                String name = "anonymous";
                System.out.println("Nice to meet you "+name);
            }
        });
        Spine spine = new Spine();
        spine.on("bye", bye);
        // default reflex for hello
        spine.is("hello").is("bye");
    }
}
```

> Output : [@Runnable](http://runnable.com/VSUPLNBGR5REcSX6/anonymous-class-for-java)

```
[hello] : {}
Name: anonymous
Nice to meet you anonymous
```

### Reflex Method

> Main.java

```java
package main;

// required modules
import java.util.*;
import org.event.*;

public class Main {
    
    public static void helloReflex(String stimulus, Map args) {
    	System.out.println("Lets get to work");
    }
    
    public void byeReflex(String stimulus, Map args) {
        System.out.print("Name: ");
        Scanner in = new Scanner(System.in);
        String name = in.next();
        System.out.println("Nice to meet you "+name);
    }
    
    public static void main(String[] args) {
    	Main main = new Main();
        Spine spine = new Spine();
        // static reaction method
        spine.on("hello", new Reflex(Main.class, "helloReflex"));
        // instance reaction method 
        // speed can be indicated manually as well
        spine.on("bye", new Reflex(main, "byeReflex").speed("slow"));
        spine.is("hello");
        // slow reactions trigger asynchronously
        spine.is("bye");
        System.out.println("ok?");
    }
}
```

> Output : [@Runnable](http://runnable.com/VSUsXM5pKB5H1mm9/reflex-method-for-java)

```
Lets get to work
ok?
Name: anonymous
Nice to meet you anonymous
```

### Reflex Class

> Introducer.java

```java
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
```

> Main.java

```java
package main;

// required modules
import org.event.*;

public class Main {

    public static void main(String[] args) {
        Introducer introducer = new Introducer();
        // static reflex methods are triggered
        Spine spine1 = new Spine(Introducer.class);
        spine1.is("hello").is("bye");
        System.out.println();
        // instance methods are triggered
        Spine spine2 = new Spine(introducer);
        spine2.is("hello").is("bye");
        System.out.println();
        // import spine1 to spine2 (or any Map<String, Reactable>)
        spine2.on(spine1);
        spine2.is("hello").is("bye");
    }
}
```

> Output : [@Runnable](http://runnable.com/VSUt5H5lLUtHHG_S/reflex-class-for-java)

```
Lets get to work
[bye] : {}

[hello] : {}
Name: anonymous
Nice to meet you anonymous

Lets get to work
Name: anonymous
Nice to meet you anonymous
```

### Event Loop

> Introducer.java

```java
package main;

// required modules
import java.util.concurrent.*;
import java.util.*;
import org.event.*;

public class Introducer extends Thread implements Reflexive {

    public Spine spine;
    BlockingQueue<Object[]> events;

    public Introducer() {
    	spine = new Spine(this);
    	events = new LinkedBlockingQueue<Object[]>();
    }
    
    public void onHello(String stimulus, Map args) {
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
```

> Main.java

```java
package main;

// required modules
import org.event.*;

public class Main {

    public static void main(String[] args) {
        Introducer introducer = new Introducer();
        introducer.start();
        Spine spine = new Spine();
        spine.on(introducer.spine.keySet(), introducer);
        // onHello & onBye run on introducer thread
        spine.is("hello").is("bye");
        // introducer thread still running
    }
}
```

> Output : [@Runnable](http://runnable.com/VSUvgkXKRJZH3R9H/event-loop-for-java)

```
Lets get to work
Name: anonymous
Nice to meet you anonymous
```

<br/>

## Reference

### Spine

Represents stimuli with associated reflexes. <br/>
When a stimulus occurs, appropriate reflexes are triggered.

| **Spine** | `Map<String, Set<Reflexive>>` |
|-----------|-------------------------------|
| **Spine** <br/>                                                                                                          `()`, `(cls)`, `(obj)` |                                                                                                     Create spine : empty, with reflexes from class, or object <br/>                                                             `Spine spine = new Spine();` <br/>                                                                                           `Spine stimuli = new Spine(NetHandler.class);` <br/>                                                                         `Spine stimuli = new Spine(netHandlerObj);` |
| **on** <br/>                                                                                                               `(stimulus, reflex)`, <br/> `(stimulus, reflexes)`, <br/> `(stimuli, reflex)`, <br/> `(assoc)` |                             Set reflexes to trigger on stimuli <br/>                                                                                     `spine.on("write-done", writeDoneReflex);` <br/>                                                                             `spine.on(multipleDone, doneReflex);` <br/>                                                                                  `spine.on("done", doneReflexes);` <br/>                                                                                      `spine.on(anotherSpine);` |
| **off** <br/>                                                                                                              `(stimulus, reflex)`, <br/> `(stimulus, reflexes)`, <br/> `(stimuli, reflex)`, <br/> `(assoc)` |                             Turn off reflexes for stimuli <br/>                                                                                          `spine.off("write-done", writeDoneReflex);` <br/>                                                                            `spine.off(multipleDone, doneReflex);` <br/>                                                                                 `spine.off("done", doneReflexes);` <br/>                                                                                     `spine.off(anotherSpine);` |
| **is** <br/>                                                                                                               `(stimulus, args)` <br/> `(stimulus, args...)` |                                                                             indicate a stimulus, causing reflexes to trigger <br/>                                                                       `spine.is("write", argsMap);` <br/>                                                                                          `spine.is("write", "err", e, "data", data);` |
| *fallback* <br/> `()`, `(fallback)`                                                                                        | get / set fallback reflex, for stimulus with no reflexes <br/>                                                             `Reactable fallback = spine.fallback();` <br/>                                                                               `spine.fallback(myFallbackReaction);` |

<br/>

### Reflex

Response to a stimulus. <br/>
Encapsulates a fast or <i>slow</i> response. <br/>
Slow response is run on a separate thread from threadpool.

| **Reflex** | `(Reflexive)` |
|------------|---------------|
| **Reflex** <br/>                                                                                                         `(reflexive)`, `(cls, mthd)`, <br/> `(obj, mthd)` |                                                                          Create a reflex that can be invoked on a stimulus <br/>                                                                    `Reflex done = new Reflex(reflexive);` <br/>                                                                                 `Reflex done = new Reflex(MthdCls.class, "mthd");` <br/>                                                                     `Reflex done = new Reflex(MthdObj, "mthd");` |
| **speed** <br/>                                                                                                            `()`, `(speed)` |                                                                                                            Get / set speed of reflex <br/>                                                                                              `String speed = reflex.speed();` <br/>                                                                                       `reflex.speed("slow");` |
| **on** <br/>                                                                                                               `(stimulus, args)` |                                                                                                         Invoke the response. *(internal)* |

<br/>

### (Reflexive)

Interface for response to stimulus. <br/>
Classes implementing this interface can to respond to stimuli.

| **(Reflexive)** |      |
|-----------------|------|
| **on** <br/>                                                                                                               `(stimulus, args)` |                                                                                                         Respond to a stimulus. |

<br/>

### @Speed

Annotation to specify speed of a method. <br/>
Use `@Speed("fast")` for fast method (optional) <br/>
Use `@Speed("slow")` for slow method *(required)*

| **(@Speed)** |     |
|--------------|-----|
| **value** <br/>                                                                                                            `()`, `(speed)` |                                                                                                            Speed of method (fast or slow). |

<br/>
