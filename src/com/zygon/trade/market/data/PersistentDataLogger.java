/**
 *
 */
package com.zygon.trade.market.data;

import com.zygon.trade.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public abstract class PersistentDataLogger<T> implements DataLogger<T> {

    private static final Logger logger = LoggerFactory.getLogger(PersistentDataLogger.class);
    private Database database = null;
    
    protected abstract void doLog(T data);

    protected final Database getDatabase() {
        return this.database;
    }
    
    @Override
    public void log(T data) {
        if (this.database != null) {
            try {
                this.doLog(data);
            } catch (Throwable th) {
                logger.error("Error storing data: " + data, th);
            }
        }
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
