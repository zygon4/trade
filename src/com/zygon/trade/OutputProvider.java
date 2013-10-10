/**
 * 
 */

package com.zygon.trade;

/**
 *
 * @author zygon
 */
public interface OutputProvider {
    
    // This interface should probably just be "getOutput()" and that's it.
    // Now we're bleeding in some extra-curricular items such as commands
    // and navigation for that matter.
    
    public String getDisplayname();
    
    public String getNavigationElementToken();
    
    /**
     * Returns an output Object.
     * @param input - a collection of (possibly empty) input items. 
     * @return an output Object.
     */
    public Response getOutput(Request request);
}
