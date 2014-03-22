/**
 * 
 */

package com.zygon.trade.modules.data;

import com.zygon.data.Handler;
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

    public <T> void register (Handler<T> reg) {
        for (Module child : this.getModules()) {
            DataFeed<T> feed = (DataFeed) child;
            feed.register(reg);
        }
    }
    
    @Override
    public void uninitialize() {
        
    }
    
    public <T> void unregister (Handler<T> reg) {
        for (Module child : this.getModules()) {
            DataFeed<T> feed = (DataFeed<T>) child;
            feed.unregister(reg);
        }
    } 
}
