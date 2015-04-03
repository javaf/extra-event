// @wolfram77
package org.event;

// required modules
import java.lang.annotation.*;



/**
 * Annotation to specify speed of a method. <p>
 * Use {@code @Speed("fast")} for fast method (optional), or
 * {@code @Speed("slow")} for slow method <i>(required)</i>. </p>
 * @author wolfram77
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Speed {
    
    /**
     * Speed of method <i>fast / slow</i>.
     * @return speed of method
     */
    String value() default "fast";
}
