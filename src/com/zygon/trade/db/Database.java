/**
 * 
 */

package com.zygon.trade.db;

/**
 * TODO: you know.. getters and setters
 * 
 * @author zygon
 */
public interface Database {
    
    /**
     * Opaque method of adding a storage space. 
     * 
     * @param options runtime implementation specific options.
     */
    public void addStorage(DatabaseMetadata metaData);
    
    /**
     * Returns this Database's name.
     * @return 
     */
    public String getName();
    
    /**
     * Initializes the Database with the given options.
     * 
     * @param options runtime implementation specific options.
     */
    public void initialize(DatabaseMetadata metaData);
    
    /**
     * Uninitializes the entire Database.
     */
    public void uninitialize();
}
