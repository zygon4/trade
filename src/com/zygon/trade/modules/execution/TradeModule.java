/**
 *
 */
package com.zygon.trade.modules.execution;

import com.zygon.trade.Module;
import com.zygon.trade.Request;
import com.zygon.trade.strategy.TradeAgent;
import com.zygon.trade.strategy.TradeSummary;

/**
 *
 * @author zygon
 */
public class TradeModule extends Module {

    private static final String TRADE_SUMMARY = "sum";
    
    private final ExecutionModule execModule;    

    /*pkg*/ TradeModule(String name, ExecutionModule execModule) {
        super("Trade-"+name);
	this.execModule = execModule;
    }
    
    @Override
    public Module[] getModules() {
	return null;
    }

    private boolean isTradeSummaryRequest(Request request) {
        return request.isCommand(TRADE_SUMMARY);
    }
    
    @Override
    public Object getOutput(Request request) {
        
        String output = null;
        
        if (request.isCommandRequest()) {
            if (this.isTradeSummaryRequest(request)) {
                StringBuilder sb = new StringBuilder();
                
                for (TradeAgent trader : this.execModule.getInformationModule().getAgents()) {
                    sb.append(trader.getAgentSummary()).append('\n');
                    
                    for (TradeSummary summary : trader.getTradeSummary()) {
                        sb.append(" - ").append(summary).append('\n');
                    }
                }
                
                output = sb.toString();
            } else {
                output = "Unknown command: " + request.getCommand();
            }
        } else if (request.isListCommandRequest()) {
            output = String.format(" - %s", TRADE_SUMMARY);
        }
        
        return output;
    }
    
    @Override
    public void initialize() {

    }

    @Override
    public void uninitialize() {

    }
}
