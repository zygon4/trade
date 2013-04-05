/**
 * 
 */

package com.zygon.exchange.modules.feed;

import com.zygon.exchange.Module;

/**
 *
 * @author zygon
 */
public class MtGoxFeed extends Adapter {

    private final Module[] modules = new Module[] {
        new MtGoxFeed()
    };
    
    public MtGoxFeed() {
        super ("MtGoxFeed");
    }
    
    @Override
    public Module[] getModules() {
        return null;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }
}
