// @wolfram77
package org.event;

// required modules
import java.lang.annotation.*;



/**
 * React annotation for specifying reaction mode
 * @author wolfram77
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface React {
    
    /**
     * Reaction mode: "sync" or "async"
     * @return reaction mode
     */
    public String mode() default "sync";
}
