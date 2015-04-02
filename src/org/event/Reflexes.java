// @wolfram77
package org.event;

// required modules
import java.lang.annotation.*;



/**
 * <b>Annotation to specify reaction-method speed</b>
 * <div>Use {@code @Reflexes("fast")} for fast reaction-method (optional)</div>
 * <div>Use {@code @Reflexes("slow")} for slow reaction-method (required)</div>
 * @author wolfram77
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Reflexes {
    
    /**
     * <b>Speed of reaction-method ("fast" or "slow")</b>
     * @return speed of reaction-method
     */
    String value() default "fast";
}
