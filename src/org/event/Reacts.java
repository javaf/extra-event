// @wolfram77
package org.event;

// required modules
import java.lang.annotation.*;



/**
 * Annotation to specify {@linkplain Reaction} method speed <br/>
 * Reacts("fast") is optional, Reacts("slow") is required
 * @author wolfram77
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Reacts {
    
    /**
     * Speed of {@linkplain Reaction} method ("fast" or "slow")
     * @return speed of {@linkplain Reaction} method
     */
    public String speed() default "fast";
}
