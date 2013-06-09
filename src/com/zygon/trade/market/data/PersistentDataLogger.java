/**
 *
 */
package com.zygon.trade.market.data;

import com.zygon.trade.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class PersistentDataLogger<T> implements DataLogger<T> {

    private static final Logger logger = LoggerFactory.getLogger(PersistentDataLogger.class);
    private Database database = null;
    
    @Override
    public void log(T data) {
        try {
            this.database.store(data);
        } catch (Throwable th) {
            this.logger.error("Error storing data: " + data, th);
        }
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
