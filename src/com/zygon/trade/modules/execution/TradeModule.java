/**
 *
 */
package com.zygon.trade.modules.execution;

import com.zygon.trade.Module;

/**
 *
 * @author zygon
 */
public class TradeModule extends Module {

    private final ExecutionModule execModule;    

    /*pkg*/ TradeModule(String name, ExecutionModule execModule) {
        super("Trade-"+name);
	this.execModule = execModule;
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
