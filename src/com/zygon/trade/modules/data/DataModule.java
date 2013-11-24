/**
 * 
 */

package com.zygon.trade.modules.data;

import com.zygon.data.EventFeed;
import com.zygon.trade.Module;
import com.zygon.trade.ParentModule;

/**
 *
 * @author zygon
 */
public class DataModule extends ParentModule {

    public static final String ID = "data";
    
    public DataModule() {
        super (ID, null, DataFeed.class);
    }
    
    @Override
    public void initialize() {
        
    }

    public void register (EventFeed.Handler reg) {
        for (Module child : this.getModules()) {
            DataFeed feed = (DataFeed) child;
            feed.register(reg);
        }
    }
    
    @Override
    public void uninitialize() {
        
    }
    
    public void unregister (EventFeed.Handler reg) {
        for (Module child : this.getModules()) {
            DataFeed feed = (DataFeed) child;
            feed.unregister(reg);
        }
    } 
}
