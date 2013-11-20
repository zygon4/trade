
package com.zygon.trade.modules.execution.broker;

import com.zygon.trade.Configuration;
import com.zygon.trade.Module;
import com.zygon.trade.execution.exchange.simulation.SimulationExchange;
import com.zygon.trade.trade.TradeBroker;

/**
 *
 * @author zygon
 */
public class Broker extends Module {

    private TradeBroker broker;
    
    public Broker(String name) {
        super(name, null, null);
    }

    @Override
    protected void doWriteStatus(StringBuilder sb) {
        this.writeTradeSummary(sb);
    }

    public TradeBroker getBroker() {
        return this.broker;
    }

    @Override
    public void initialize() {
        Configuration config = this.getConfiguration();
        String accountId = config.getValue("accountId");
        
        // TODO: exchange factory
        this.broker = new TradeBroker(accountId, SimulationExchange.createInstance());
        this.broker.getExchange().start();
    }

    @Override
    public void uninitialize() {
        this.broker.getExchange().stop();
    }
    
    /*pkg*/ void writeTradeSummary(StringBuilder sb) {
        sb.append("Active trades ").append(this.broker.getActiveTradeCount()).append('\n');
        sb.append("Cancelled trades ").append(this.broker.getTradeSummary().getCancelledTradeCount()).append('\n');
        sb.append("Total finish trades ").append(this.broker.getTradeSummary().getTotalTradeCount()).append('\n');
    }
}
