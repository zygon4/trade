/**
 * 
 */

package com.zygon.trade.db;

import java.io.Closeable;
import java.util.Collection;

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
    
    public <T> Collection<T> retrieve(Class<T> cls, String query);
    
    public void store(Object object);
}
