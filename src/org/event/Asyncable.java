// @wolfram77
package org.event;



public interface Asyncable {
    
    // Async ()
    // - check if runs asynchronously
    boolean async();
    
    
    // Async (async)
    // - set if runs asynchronously
    Asyncable async(boolean async);
}
