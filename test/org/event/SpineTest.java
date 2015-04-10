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
    
    public static void done() {
        try { Thread.sleep(10); }
        catch(InterruptedException e) {}
        System.out.println("\n");
    }

    @Test
    public void test_DefaultReflex() {
        System.out.println("test - default reflex");
        Spine spine = new Spine();
        // trigger default reaction
        spine.is("hot-object", "msg", "Ouch!");
        done();
    }


    @Test
    public void test_Reflexive() {
        System.out.println("test - reflexive");
        HelloReflex hello = new HelloReflex();
        // annotations only work when encapsulated by Reflex
        Reflexive bye = new Reflex(new ByeReflex());
        Spine spine = new Spine();
        // chaining method calls is supported
        spine.on("hello", hello).on("bye", bye);
        spine.is("hello").is("bye");
        done();
    }


    @Test
    public void test_MethodReference() {
        System.out.println("test - method reference");
        Spine spine = new Spine();
        spine.on("hello", ReflexMethods::onHello);
        // set method speed manually
        spine.on("bye", new Reflex(new ReflexMethods()::onBye).speed("slow"));
        // chaining method calls is supported
        spine.is("hello").is("bye");
        done();
    }


    @Test
    public void test_AnonymousReflexive() {
        System.out.println("test - anonymous reflexive");
        // anonymous classes are easy to write
        Reflexive hello = new Reflex(new Reflexive() {
            @Override
            public void on(String stimulus, Map args) {
                System.out.println("Anonymous: Lets get to work");
            }
        });
        // annotations allowed in anonymous class
        Reflexive bye = new Reflex(new Reflexive() {
            @Override
            @Speed("slow")
            public void on(String stimulus, Map args) {
                String name = "anonymous";
                System.out.println("Anonymous: Nice to meet you "+name);
            }
        });
        Spine spine = new Spine();
        // chaining method calls is supported
        spine.on("hello", hello).on("bye", bye);
        spine.is("hello").is("bye");
        done();
    }


    @Test
    public void test_LambdaExpression() {
        System.out.println("test - lamdba expression");
        // anonymous classes are easy to write
        Reflexive hello = new Reflex((String stimulus, Map args) -> {
            System.out.println("lamdba: Lets get to work");
        });
        // annotations allowed in anonymous class
        Reflexive bye = new Reflex((String stimulus, Map args) -> {
            String name = "anonymous";
            System.out.println("lamdba: Nice to meet you "+name);
        });
        Spine spine = new Spine();
        // chaining method calls is supported
        spine.on("hello", hello).on("bye", bye);
        spine.is("hello").is("bye");
        done();
    }


    @Test
    public void test_ReflexMethod() {
        System.out.println("test - reflex method");
        Spine spine = new Spine();
        // static reaction method
        spine.on("hello", new Reflex(ReflexMethods.class, "onHello"));
        // instance reaction method 
        // speed can be indicated manually as well
        spine.on("bye", new Reflex(new ReflexMethods(), "onBye").speed("slow"));
        spine.is("hello");
        spine.is("bye");
        // slow reactions trigger asynchronously
        done();
    }


    @Test
    public void test_ReflexClass() {
        System.out.println("test - reflex class");
        ReflexClass reflexClass = new ReflexClass();
        // static reaction methods are triggered
        Spine spine1 = new Spine(ReflexClass.class);
        spine1.is("hello").is("bye");
        System.out.println();
        // instance methods are triggered
        Spine spine2 = new Spine(reflexClass);
        spine2.is("hello").is("bye");
        System.out.println();
        // import spine1 to spine2 (or any Map<String, Reflexive>)
        spine2.on(spine1);
        spine2.is("hello").is("bye");
        done();
    }


    @Test
    public void test_EventLoop() {
        System.out.println("test - event loop");
        EventLoop eventLoop = new EventLoop();
        eventLoop.start();
        Spine spine = new Spine();
        spine.on(eventLoop.spine.keySet(), eventLoop);
        spine.is("hello").is("bye");
        done();
    }
}
