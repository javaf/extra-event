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
> You now `emit` this `event` to **Harish**, who `listens` to this event. He jumps to action.
> Rapidly tiptoeing he enters the class and in a minute he is beside you. Opens up a notebook with
> some previously scribbled pages, and a pen suddenly pops into his hand. You, the `event emitter`,
> and **Harish**, the `event listener` have to wait for a full five minutes until **Sambhu** is back.

> *Huh!* **Sambhu** is shocked, and suddenly he turns into a tiger with his paws open. Didn't i tell he has
> his eyes and ears on everyone? *BAM! BAM!*

Okay, that was bad. But atleast you now know that we use events in our life. In fact we use it
all the time. If you have ever heard of `interrupts`, they are essentially `hardware triggered events`,
which provide a great solution to monitoring something without repeated polling. `Software events`
are similar, except that the triggering of the event is performed by software as well.

`Emitting` an event is similar to *making a function call*, and `listening` is the act of
*getting a function call*. How is it different from a function call then? Well, with the idea
of events, there can be any number of *event listeners*, and as many *types of events* as you like.
A more `dynamic form of function call`.



## Usage

Shall we get started? The interface of `java-event-emitter` is similar to that of `EventEmitter`
in `Node.js`, which is a great javascript-based server language. Go through the *examples*,
download the source code `src/org/data` and `src/org/event`, add it to your *project* and
follow the *reference*.



## Examples

### Hello World!
```java
// required modules
import org.event.*;

public class Main {
    
    public static void main(String[] args) {
        EventEmitter event = new EventEmitter();       // create event emitter
        event.emit("hello", "msg", "Hello World!");    // DefaultEventListener listens
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

### Event Listener

```java
// required modules
import org.event.*;

class HelloTeller implements IEventListener {
    
    @Override
    public void listen(String event, Map args) {
        System.out.println("Hello event "+event);
    }
}

class ByeTeller implements IEventListener {
    
    @Override
    public void listen(String event, Map args) {
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


## Reference

| `class EventEmitter`         |                                                                      |
|------------------------------|----------------------------------------------------------------------|
| **EventEmitter** <br/> `()`                                                                                                | create event emitter <br/>                                                                                               `EventEmitter event = new EventEmitter()`                    |
| **add**          <br/> `("event", listener)`                                                                               | add listener to an event <br/>                                                                                   `event.add("write-done", writeDoneListener);`                |
| **emit**         <br/> `("event", "argument", value, ...)`                                                                 | emit an normal or error event <br/>                                                                                  `event.emit("write", "time", new Date(), "data", data); // normal` <br/>                                              `event.emit("write", "err", e, "data", data); // error`      |
| **fallback**     <br/> `()`, `(listener)`                                                                                  | get or set fallback listener incase an event has no listeners <br/>                                               `DefaultEventListener` is default fallback <br/>                                                           `event.fallback(myFallbackListener); // `                    |
| **remove**       <br/> `()`, `("event")`, `("event", listener)`                                                            | remove all listeners / all of specific event / specific <br/>                                                      `event.remove("write", writeDoneListener);` <br/>                                                            `event.remove("write");`                                     |

| `interface IEventListener`   |                        |
|------------------------------|------------------------|
| **listen**       <br/> `("event", arguments)`                                                                              | called when object implementing this interface is set as listener <br/>                                                   one listener can be attached to multiple events, hence `"event"`|

| `class DefaultEventListener` | `implements IEventListener`  |
|------------------------------|------------------------------|
| **listen**       <br/> `("event", arguments)`                                                                              | called when an event has no other listener and fallback is unchanged |

