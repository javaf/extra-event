// @wolfram77
package main;

// required modules



public class Main {
    
    public static void print(Object... str) {
        for(Object item : str)
            System.out.println(item);
    }
    
    public static void print2(Object... str) {
        print(str);
    }
    
    public static void main(String[] args) throws Throwable {
        print2("a", "b");
    }
}
