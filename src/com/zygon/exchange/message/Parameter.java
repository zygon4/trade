/**
 * 
 */

package com.zygon.exchange.message;

/**
 *
 * @author zygon
 */
public interface Parameter {
    
    /*
     * There should really only be a few tags total at a global level
     * - ID
     * - Context
     * - ...?
     */
    
    public int getTag();
    public int getDataLength();
    public int getDataOffset();
}
