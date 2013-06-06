/**
 * 
 */

package com.zygon.trade.db;

/**
 *
 * @author zygon
 */
public interface DatabaseMetadata {
    /**
     * Returns a property give the property name.
     * @param name the property name.
     * @return a property give the property name.
     */
    public String getProperty(String name);
}
