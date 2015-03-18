// @wolfram77
package org.event;

// required modules
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;



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
    public void test_EventLoop() {
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
