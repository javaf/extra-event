// @wolfram77
package org.event;

// required modules
import java.lang.annotation.*;



/**
 * <b>Annotation to specify reflex speed</b>
 * <div>Use {@code @Speed("fast")} for fast reflex (optional)</div>
 * <div>Use {@code @Speed("slow")} for slow reflex <strong>(required)</strong></div>
 * @author wolfram77
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Speed {
    
    /**
     * <b>Speed of reflex ("fast" or "slow")</b>
     * @return speed of reflex
     */
    String value() default "fast";
}
