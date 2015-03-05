// @wolfram77
package main;

// required modules
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import org.event.*;



public class Main {
    
    public void print(String str) {
        System.out.println(str);
    }
    
    public static void main(String[] args) throws Throwable {
        Method m = Main.class.getMethod("print", String.class);
        MethodHandle mh = MethodHandles.lookup().unreflect(m);
        mh.invokeExact(new Main(), "abc");
    }
}
