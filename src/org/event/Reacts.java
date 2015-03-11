// @wolfram77
package org.event;

// required modules
import java.lang.annotation.*;



/**
 * <h3>Annotation to specify reaction-method speed</h3>
 * Reacts("fast") is optional <br/>
 * Reacts("slow") is required
 * @author wolfram77
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Reacts {
    
    /**
     * <h3>Speed of reaction-method ("fast" or "slow")</h3>
     * @return speed of {@linkplain Reaction} method
     */
    public String speed() default "fast";
}
