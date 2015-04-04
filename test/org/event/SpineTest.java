// @wolfram77
package org.event;

// required modules
import java.util.*;
import org.junit.*;
import org.event.part.*;



public class SpineTest {
    
    public SpineTest() {}
    
    @BeforeClass
    public static void setUpClass() {}
    
    @AfterClass
    public static void tearDownClass() {}
    
    @Before
    public void setUp() {}
    
    @After
    public void tearDown() {}


    @Test
    public void test_StandardStimulus() {
        System.out.println("test - standard stimulus");
        Spine spine = new Spine();
        // trigger default reaction
        spine.is("hot-object", "msg", "Ouch!");
        System.out.println("\n");
    }


    @Test
    public void test_ErrorStimulus() {
        System.out.println("test - error stimulus");
        Spine spine = new Spine();
        try { throw new RuntimeException("Got a Sprain"); }
        catch(Exception e) {
            Exception ex = null;
            try { spine.is("injury", "err", e, "msg", "Cant go to school"); }
            catch(Exception exx) { ex = exx; }
            if(ex == null) throw new RuntimeException();
        }
        // err argument indicates it is an error stimulus
        System.out.println("\n");
    }


    @Test
    public void test_ReactableClass() {
        System.out.println("\n\ntest_ReactableClass");
        HelloReflex helloReflex = new HelloReflex();
        // annotations only work in Reflexive objects
        Reflexive byeReflex = new Reflex(new ByeReflex());
        Spine spine = new Spine();
        // chaining method calls is supported
        spine.on("hello", helloReflex).on("bye", byeReflex);
        spine.is("hello").is("bye");
    }


    @Test
    public void test_AnonymousAndLambdaReactable() {
        System.out.println("\n\ntest_ReactableAnonymousClass");
        // lambda expression is simpler
        Reflexive helloReaction = (String stimulus, Map args) -> {
            System.out.println("Lets get to work");
        };
        // annotations allowed in anonymous class, but not lambda expression
        Reflexive byeReaction = new Reflex(new Reflexive() {
            @Override
            @Speed("slow")
            public void on(String stimulus, Map args) {
                String name = "anonymous";
                System.out.println("Nice to meet you "+name);
            }
        });
        Spine spine = new Spine();
        // chaining method calls is supported
        spine.on("hello", helloReaction).on("bye", byeReaction);
        spine.is("hello").is("bye");
    }


    @Test
    public void test_ReactionMethod() {
        System.out.println("\n\ntest_ReactionMethod");
        ReflexClass main = new ReflexClass();
        Spine spine = new Spine();
        // static reaction method
        spine.on("hello", ReflexClass::onHello);
        // instance reaction method 
        // speed can be indicated manually as well
        spine.on("bye", new Reflex(main::onBye));
        spine.is("hello");
        spine.is("bye");
        // slow reactions trigger asynchronously
        System.out.println("ok?");
    }


    @Test
    public void test_ReactionClass() {
        System.out.println("\n\ntest_ReactionClass");
        ReflexClass reactionClass = new ReflexClass();
        // only static reaction methods are triggered
        Spine spine1 = new Spine();
        spine1.is("hello").is("bye");
        System.out.println();
        // both static and instance methods are triggered
        Spine spine2 = new Spine();
        spine2.is("hello").is("bye");
        System.out.println();
        // import spine1 to spine2 (or any Map<String, Reflexive>)
        spine2.on(spine1);
        spine2.is("hello").is("bye");
    }


    @Test
    public void test_EventLoop() {
        System.out.println("\n\ntest_EventLoop");
        EventLoop eventLoop = new EventLoop();
        Spine spine = new Spine();
        spine.on(eventLoop.spine.keySet(), eventLoop);
        spine.is("hello").is("bye");
        System.out.println();
    }
}
