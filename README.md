# java-event-emitter

What is `java-event-emitter` you ask? Wait, lets first read a short story.

> Imagine that you are in the middle chemistry class, jotting down notes on **Radicals** from the
> blackboard. The master is endlessly scribbling symbols of elements, of various valencies, with
> an indicated charge quantity on top. **Harish**, your benchmate is yet to enter the class!

> He is waiting for **Sambhu**, the master, to somehow get distracted, so he can find a way to sneak
> into the class and have his attendence marked. But, that is utterly impossible. **Sambhu** is a
> very alert man. He has his eyes and ears on everyone. So, **Harish** is waiting at the other end of
> the building, else **Sambhu** might see him.

> *Woof, Woofoof!* **Sambhu** cries coughing out smoke from his mouth, or is it? *"I will be back
> in a minute. Let there be pinpoint silence"*, **Sambhu** somehow speaks out. This is an `event`!
> You now `emit` this `event` to **Harish**, who `absorbs` this event. He then jumps to action.
> Rapidly tiptoeing he enters the class and in a minute he is beside you. Opens up a notebook with
> some previously scribbled pages, and a pen suddenly pops into his hand. You, the `event emitter`,
> and **Harish**, the `event absorber` have to wait for a full five minutes until **Sambhu** is back.

> *Huh!* **Sambhu** is shocked, and suddenly he turns into a tiger with his paws open. Didn't i tell he has
> his eyes and ears on everyone? *BAM! BAM!*

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

| **EventEmitter** | `Map<String, Set<IEventAbsorber>>` |
|------------------|------------------------------------|
| **EventEmitter** <br/> `()`, `(cls)`, `(obj)`                                                                              | create event emitter, opt. from a class or object <br/>                                                                    `EventEmitter event = new EventEmitter();` <br/>                                                                             `EventEmitter event = new EventEmitter(NetHandler.class);` <br/>                                                             `EventEmitter event = new EventEmitter(netHandlerObj);` |
| **add** <br/> `(event, eventer)`, <br/> `(event, eventers)`, <br/> `(events, eventer)`, <br/> `(map)`                      | add eventers to events <br/>                                                                                   `event.add("write-done", writeDoneEventer);` |
| **remove** <br/> `(event, eventer)`, <br/> `(event, eventers)`, <br/>                                                      `(events, eventer)`, <br/> `(map)`, `(event)`, <br/> `()`                                                                    | remove eventers from events <br/>                                                                                          `event.remove("write", writeDoneEventer);` <br/>                                                                             `event.remove("write");` |
| **emit** <br/> `(event, args)` <br/> `(event, args...)`                                                                    | emit normal / error event <br/>                                                                                        `event.emit("write", "time", new Date(), "data", data);` <br/>                                                               `event.emit("write", "err", e, "data", data);` |
| *fallback* <br/> `()`, `(absorbers)`                                                                                       | get / set fallback eventer (for events with no eventers) <br/>                                                            *DefEventer* is default fallback <br/>                                                                                       `Eventable eventer = event.fallback();` <br/>                                                                          `event.fallback(myFallbackEventer);` |

<br/>

| **(Eventable)** |                        |
|-----------------|------------------------|
| **absorb** <br/> `(event, args)`                                                                                           | called when object implementing this interface is set as eventer <br/>                                                   one eventer can be attached to multiple events, hence *event*|

<br/>

| **Eventer** | `(Eventable)`  |
|-------------|----------------|
| **EventAbsorber** <br/> `(cls, mthd)`, <br/> `(obj, mthd)`                                                                 | create an eventer from a static on instance method <br/>                                                            `event.add("event0", new Eventer(MthdCls.class, "mthd"));` <br/>                                                 `event.add("event0", new Eventer(MthdObj, "mthd"));` |

<br/>

| **DefEventer** | `(Eventable)`  |
|----------------|----------------|
| **absorb** <br/> `(event, args)`                                                                                           | absorbs events with no absorbers |
