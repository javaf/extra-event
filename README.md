# java-event-emitter

Imagine that you are in the middle chemistry class, jotting down notes on **Radicals** from the
blackboard. The master is endlessly scribbling symbols of elements, of various valencies, with
an indicated charge quantity on top. *Harish*, your benchmate is yet to enter the class!

He is waiting for *Sambhu*, the master, to somehow get distracted, so he can find a way to sneak
into the class and have his attendence marked. But, that is utterly impossible. *Sambhu* is a
very alert man. He has his eyes and ears on everyone. So, Harish is waiting at the other end of
the building, else *Sambhu* might see him.

**Woof, Woofoof!** *Sambhu* cries coughing out smoke from his mouth, or is it? *"I will be back
in a minute. Let there be pinpoint silence"*, *Sambhu* somehow speaks out. This is an **event**!
You now **emit** this **event** to *Harish*, who LISTENs to this event. He jumps to action.
Rapidly tiptoeing he enters the class and in a minute he is beside you. Opens up a notebook with
some previously scribbled pages, and a pen suddenly pops into his hand. You, the EVENT EMITTER,
and Harish, the EVENT LISTENER have to wait for a full five minutes until Sambhu is back.

Huh! Sambhu is shocked, and suddenly he turns into a tiger with his paws open. Didn't i tell he has
his eyes and ears on everyone? BAM! BAM!

Okay, that was bad. But atleast you now know that we use events in our life. In fact we use it
all the time. If you have ever heard of interrupts, they are essentially hardware triggered events,
which provide a great solution to monitoring something without repeated polling. Software events
are similar, except that the triggering of the event is performed by software as well.

Emitting an event is similar to making a function call, and listening is the act of getting a
function call. How is it different from a function call then? Well, with the idea of events,
there can be any number of event listeners, and as many types of events as you like. A more
dynamic form of function call.



## Usage

Shall we get started? The interface of java-event-emitter is similar to that os EventEmitter
in Node.js, which is a great javascript-based server language. Go through the examples,
download the source code, add it to your project and follow the reference.




