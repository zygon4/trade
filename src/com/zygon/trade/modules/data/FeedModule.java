
package com.zygon.trade.modules.data;

import com.zygon.trade.Module;
import com.zygon.trade.Schema;

/**
 *
 * @author zygon
 */
public class FeedModule extends Module {

    private static Schema SCHEMA = new Schema("data_feed.json");
    
    public FeedModule(String name) {
        super(name, SCHEMA, null);
    }

    @Override
    public Module[] getModules() {
        return null;
    }

    @Override
    public void initialize() {
        // TODO:
    }

    @Override
    public void uninitialize() {
        // TODO:
    }
}
