/**
 * 
 */

package com.zygon.exchange.modules.feed;

import com.zygon.exchange.Module;
import com.zygon.exchange.net.Listener;

/**
 *
 * @author zygon
 */
/*pkg*/ abstract class Adapter extends Module {

    private FListener listener;
    
    protected Adapter(String name) {
        super (name);
    }

    @Override
    public void initialize() {
        
        if (this.listener != null) {
            throw new IllegalStateException("Adapter is still initialized.");
        }
        
        this.listener = new Listener(this.getName(), null, null);
        this.listener.doStart();
    }

    @Override
    public void uninitialize() {
        this.listener.doStop();
        this.listener = null;
    }
}
