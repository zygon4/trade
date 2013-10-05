/**
 *
 */
package com.zygon.trade.modules.execution;

import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.ExecutionController.Binding;
import com.zygon.trade.Module;
import com.zygon.trade.modules.model.InformationModule;

/**
 *
 * @author zygon
 */
public class ExecutionModule extends Module {

    private final InformationModule informationModule;
    private final AccountModule accountModule;
    private final TradeModule tradeModule;
    private final Module[] modules;

    private final ExecutionController controller;

    public ExecutionModule(String name, InformationModule informationModule, Binding binding) {
        super("Execution-"+name);

        this.controller = new ExecutionController(binding);
        
        this.informationModule = informationModule;
	this.accountModule = new AccountModule(name, this);
	this.tradeModule = new TradeModule(name, this);
	
        this.modules = new Module[]{this.informationModule, this.accountModule, this.tradeModule};
    }
    
    /*pkg*/ ExecutionController getController() {
	return this.controller;
    }

    public InformationModule getInformationModule() {
        return this.informationModule;
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
