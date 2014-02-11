/**
 * 
 */

package com.zygon.database;

import java.io.Closeable;

/**
 * 
 * @author zygon
 */
public interface Database extends Closeable {
    
    public void addTransform(DataTransform<?> transform);
    
    /**
     * Returns this Database's name.
     * @return 
     */
    public String getName();
    
    public <K, N, V> void persist (Object obj);
    
    public <K, N, V> Object retrieve(Class<?> cls, Persistable<K, N, V> key);
    
    public void removeTransform(DataTransform<?> transform);
}
