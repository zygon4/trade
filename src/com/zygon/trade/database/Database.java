/**
 * 
 */

package com.zygon.trade.database;

import java.io.Closeable;

/**
 * 
 * @author zygon
 */
public interface Database extends Closeable {
    
    /**
     * Returns this Database's name.
     * @return 
     */
    public String getName();
    
    public <K, N, V> void persist (Object obj);
    
    public <K, N, V> Object retrieve(Class<?> cls, Persistable<K, N, V> key);
}
