/**
 *
 */
package com.zygon.trade.modules.execution;

import com.zygon.trade.Module;

/**
 *
 * @author zygon
 */
public class ExecutionModule extends Module {

    private final AccountModule accountModule;
    private final TradeModule tradeModule;
    private final Module[] modules;

    public ExecutionModule(String name) {
        super("Execution-"+name);

	this.accountModule = new AccountModule(name, this);
	this.tradeModule = new TradeModule(name, this);
	
        this.modules = new Module[]{this.accountModule, this.tradeModule};
    }
    
    @Override
    public Module[] getModules() {
	return this.modules;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void uninitialize() {

    }
}
