/**
 * 
 */

package com.zygon.trade.db;

import com.zygon.trade.db.hector.HectorDatabase;

/**
 *
 * @author zygon
 */
public class DatabaseFactory {
    public static Database get(String db) {
        if (db.equals(HectorDatabase.class.getCanonicalName())) {
            return new HectorDatabase();
        }
        
        return null;
    }
}
