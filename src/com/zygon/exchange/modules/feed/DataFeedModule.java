/**
 * 
 */

package com.zygon.exchange.modules.feed;

import com.zygon.exchange.Module;

/**
 *
 * @author zygon
 */
public class DataFeedModule extends Module {

    private final Module[] modules = new Module[] {
        new MtGoxFeed()
    };
    
    public DataFeedModule() {
        super("DataFeedModule");
    }
    
    @Override
    public Module[] getModules() {
        return this.modules;
    }

    @Override
    public void initialize() {
        for (Module module : this.modules) {
            module.initialize();
        }
    }

    @Override
    public void uninitialize() {
        for (Module module : this.modules) {
            module.uninitialize();
        }
    }
}
