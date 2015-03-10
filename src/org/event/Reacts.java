// @wolfram77
package org.event;

// required modules
import java.lang.annotation.*;



/**
 * Reacts annotation for specifying reaction
 * @author wolfram77
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Reacts {
    
    /**
     * Reaction speed: "fast" or "slow"
     * @return reaction speed
     */
    public String speed() default "fast";
}
