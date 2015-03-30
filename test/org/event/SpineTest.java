// @wolfram77
package org.event;

// required modules
import java.util.*;
import org.junit.*;



public class SpineTest {
    
    public SpineTest() {
    }
    
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    
    @Before
    public void setUp() {
    }
    
    
    @After
    public void tearDown() {
    }


    @Test
    public void test_StandardStimulus() {
        System.out.println("test_StandardStimulus");
        Spine spine = new Spine();
        // trigger default reaction
        spine.is("hot-object", "msg", "Ouch!");
    }


    @Test
    public void test_ErrorStimulus() {
        System.out.println("test_ErrorStimulus");
        Spine spine = new Spine();
        try { throw new RuntimeException("Got a Sprain"); }
        catch(Exception e) {
            Exception ex = null;
            try { spine.is("injury", "err", e, "msg", "Cant go to school"); }
            catch(Exception exx) { ex = exx; }
            if(ex == null) throw new RuntimeException();
        }
        // err argument indicates it is an error stimulus
    }


    @Test
    public void test_ReactableClass() {
        System.out.println("test_ReactableClass");
        ReactableClassHello helloReaction = new ReactableClassHello();
        // annotations only work in Reaction objects
        Reactable byeReaction = new SlowReaction(new ReactableClassBye());
        Spine spine = new Spine();
        // chaining method calls is supported
        spine.on("hello", helloReaction).on("bye", byeReaction);
        spine.is("hello").is("bye");
    }


    @Test
    public void test_AnonymousAndLambdaReactable() {
        System.out.println("test_ReactableAnonymousClass");
        // lambda expression is simpler
        Reactable helloReaction = (String stimulus, Map args) -> {
            System.out.println("Lets get to work");
        };
        // annotations allowed in anonymous class, but not lambda expression
        Reactable byeReaction = new SlowReaction(new Reactable() {
            @Override
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
        System.out.println("test_ReactionMethod");
        ReactionMethods main = new ReactionMethods();
        Spine spine = new Spine();
        // static reaction method
        spine.on("hello", ReactionMethods::helloReactor);
        // instance reaction method 
        // speed can be indicated manually as well
        spine.on("bye", new SlowReaction(main::byeReactor));
        spine.is("hello");
        spine.is("bye");
        // slow reactions trigger asynchronously
        System.out.println("ok?");
    }


    @Test
    public void test_ReactionClass() {
        System.out.println("test_ReactionClass");
        ReactionClass reactionClass = new ReactionClass();
        // only static reaction methods are triggered
        Spine spine1 = new Spine();
        spine1.is("hello").is("bye");
        System.out.println();
        // both static and instance methods are triggered
        Spine spine2 = new Spine();
        spine2.is("hello").is("bye");
        System.out.println();
        // import spine1 to spine2 (or any Map<String, Reactable>)
        spine2.on(spine1);
        spine2.is("hello").is("bye");
    }


    @Test
    public void test_EventLoop() {
        System.out.println("test_EventLoop");
        EventLoop eventLoop = new EventLoop();
        // only static reaction methods are triggered
        Spine spine1 = new Spine();
        spine1.is("hello").is("bye");
        System.out.println();
        // both static and instance methods are triggered
        Spine spine2 = new Spine();
        spine2.is("hello").is("bye");
        System.out.println();
        // import spine1 to spine2 (or any Map<String, Reactable>)
        spine2.on(spine1);
        spine2.is("hello").is("bye");
    }
}
