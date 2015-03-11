// @wolfram77
package org.event;

// required modules
import java.lang.annotation.*;



/**
 * Annotation to specify {@linkplain Reaction} method speed
 * @author wolfram77
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Reacts {
    
    /**
     * Speed of reaction ("fast" or "slow")
     * @return speed of reaction
     */
    public String speed() default "fast";
}
