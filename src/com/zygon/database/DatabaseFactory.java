/**
 * 
 */

package com.zygon.database;

import com.zygon.database.cassandra.CassandraDatabase;

/**
 *
 * @author zygon
 */
public class DatabaseFactory {
    public static Database get(String db) {
        if (db.equals(CassandraDatabase.class.getCanonicalName())) {
            return new CassandraDatabase();
        }
        
        return null;
    }
}
