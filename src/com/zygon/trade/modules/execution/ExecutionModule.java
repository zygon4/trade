/**
 *
 */
package com.zygon.trade.modules.execution;

import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.ExecutionController.Binding;
import com.zygon.trade.Module;

/**
 *
 * @author zygon
 */
public class ExecutionModule extends Module {

    private final AccountModule accountModule;
    private final TradeModule tradeModule;
    private final Module[] modules;

    private final ExecutionController controller;

    // TBD: pass a collection of Bindings
    public ExecutionModule(String name, Binding binding) {
        super("Execution-"+name);

	this.controller = new ExecutionController(binding);

	this.accountModule = new AccountModule(name, this);
	this.tradeModule = new TradeModule(name, this);
	this.modules = new Module[]{this.accountModule, this.tradeModule};
    }
    
    /*pkg*/ ExecutionController getController() {
	return this.controller;
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
