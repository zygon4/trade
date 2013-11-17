
package com.zygon.trade.modules.execution.broker;

import com.zygon.trade.Module;
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

    @Override
    public Module[] getModules() {
        return null;
    }

    public TradeBroker getBroker() {
        return this.broker;
    }

    @Override
    public void initialize() {
        this.broker.getExchange().start();
    }

    public void setBroker(TradeBroker broker) {
        this.broker = broker;
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
