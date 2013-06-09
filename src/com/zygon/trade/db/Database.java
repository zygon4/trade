/**
 * 
 */

package com.zygon.trade.db;

import java.io.Closeable;

/**
 * TODO: you know.. getters and setters
 * 
 * @author zygon
 */
public interface Database extends Closeable {
    
    /**
     * Returns this Database's name.
     * @return 
     */
    public String getName();
    
    public <T> T retrieve(Class<T> cls, Object key);
    
    public void store(Object object);
}
