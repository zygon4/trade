/**
 *
 */
package com.zygon.trade.modules.execution;

import com.zygon.trade.Module;
import com.zygon.trade.Request;
import com.zygon.trade.Response;
import com.zygon.trade.strategy.TradeAgent;
import com.zygon.trade.strategy.TradeSummary;

/**
 *
 * @author zygon
 */
public class TradeModule extends Module {

    private static final String TRADE_SUMMARY = "summary";
    private static final String TRADE_STATE = "state";
    
    private final ExecutionModule execModule;    

    /*pkg*/ TradeModule(String name, ExecutionModule execModule) {
        super("Trade-"+name);
	this.execModule = execModule;
    }
    
    @Override
    public Module[] getModules() {
	return null;
    }

    private boolean isTradeStateRequest(Request request) {
        return request.isCommand(TRADE_STATE);
    }
    
    private boolean isTradeSummaryRequest(Request request) {
        return request.isCommand(TRADE_SUMMARY);
    }
    
    @Override
    public Response getOutput(Request request) {
        
        String output = null;
        StringBuilder sb = new StringBuilder();
        
        if (request.isCommandRequest()) {
            if (this.isTradeSummaryRequest(request)) {
                for (TradeAgent trader : this.execModule.getInformationModule().getAgents()) {
                    sb.append(trader.getAgentSummary()).append('\n');
                    
                    for (TradeSummary summary : trader.getTradeSummary()) {
                        sb.append(" - ").append(summary).append('\n');
                    }
                }
            } else if (this.isTradeStateRequest(request)) {
                for (TradeAgent trader : this.execModule.getInformationModule().getAgents()) {
                    trader.getTradeState(sb);
                    sb.append('\n');
                }
            } else {
                sb.append("Unknown command: ").append(request.getCommandName());
            }
        } else if (request.isListCommandRequest()) {
            sb.append(String.format(" - %s", TRADE_STATE)).append('\n');
            sb.append(String.format(" - %s", TRADE_SUMMARY));
        }
        
        output = sb.toString();
        
        return new Response(output);
    }
    
    @Override
    public void initialize() {

    }

    @Override
    public void uninitialize() {

    }
}
