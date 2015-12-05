
package com.zygon.trade.market.data;

import com.zygon.trade.market.model.indication.Indication;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zygon
 * 
 */
public class DataSourceManager {
    
    private final Map<String,DataSource<?>> dataManagersById = new HashMap<String,DataSource<?>>();

    public void handle(DataSource mgmr, Indication r) {
        if (this.dataManagersById.containsKey(mgmr.getIdentifier())) {
            
            // TODO: lookup subscribers and send
            
        } else {
            // log debug
        }
    }
    
    public void register (DataSource<?> manager) {
        synchronized (this.dataManagersById) {
            if (this.dataManagersById.containsKey(manager.getIdentifier())) {
                throw new IllegalArgumentException("Already registered: " + manager.getIdentifier());
            }
            
            this.dataManagersById.put(manager.getIdentifier(), manager);
        }
    }
    
    // We need to specify a source, source type?, and ?
    
//    public static enum DataSourceType {
//        
//        PRICE,
//    }
    
//    public void subscribe ()
    
}
