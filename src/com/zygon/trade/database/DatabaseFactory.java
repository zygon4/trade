/**
 * 
 */

package com.zygon.trade.database;

import com.zygon.trade.database.cassandra.CassandraDatabase;

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
