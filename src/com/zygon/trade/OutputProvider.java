/**
 * 
 */

package com.zygon.trade;

import java.util.Map;

/**
 *
 * @author zygon
 */
public interface OutputProvider {
    /**
     * Returns an output Object.
     * @param input - a collection of (possibly empty) input items. 
     * @return an output Object.
     */
    public Object getOutput(Map<String, Object> input);
}
