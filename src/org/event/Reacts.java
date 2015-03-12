// @wolfram77
package org.event;

// required modules
import java.lang.annotation.*;



/**
 * <h3>Annotation to specify reaction-method speed</h3>
 * Use {@code @Reacts("fast")} for fast reaction-method (optional)<br/>
 * Use {@code @Reacts("slow")} for slow reaction-method (required)
 * @author wolfram77
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Reacts {
    
    /**
     * <h3>Speed of reaction-method ("fast" or "slow")</h3>
     * @return speed of reaction-method
     */
    String value() default "fast";
}
