/**
 * 
 */

package com.zygon.trade.db;

import com.zygon.trade.db.cassandra.CassandraDatabase;

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
