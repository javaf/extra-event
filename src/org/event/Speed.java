// @wolfram77
package org.event;

// required modules
import java.lang.annotation.*;



/**
 * <b>Annotation to specify speed of a method</b>
 * <div>Use {@code @Speed("fast")} for fast method (optional)</div>
 * <div>Use {@code @Speed("slow")} for slow method <em>(required)</em></div>
 * @author wolfram77
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Speed {
    
    /**
     * <b>Speed of method ("fast" or "slow")</b>
     * @return speed of method
     */
    String value() default "fast";
}
