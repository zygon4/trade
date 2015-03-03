
package com.zygon.trade.modules.execution.broker;

import com.zygon.configuration.Configuration;
import com.zygon.schema.StringElement;
import com.zygon.schema.parse.ConfigurationSchema;
import com.zygon.trade.Module;
import com.zygon.trade.Schema;
import com.zygon.trade.execution.exchange.simulation.SimulationExchange;
import com.zygon.trade.trade.TradeBroker;

/**
 *
 * @author zygon
 */
public class Broker extends Module {

    private static Schema createSchema() {
        
        StringElement connections = new StringElement("accountId", "Account Identifier", "");
        ConfigurationSchema configSchema = new ConfigurationSchema(Broker.class.getCanonicalName()+"_schema", "v1", connections);
        
        return new Schema(configSchema);
    }
    
    private String accountId = null;
    private TradeBroker broker;
    
    public Broker(String name) {
        super(name, createSchema(), null);
    }

    @Override
    public void configure(Configuration configuration) {
        super.configure(configuration);
        this.accountId = configuration.getStringValue("accountId");
        
        this.broker = new TradeBroker(this.accountId, SimulationExchange.createInstance());
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
        
        // TODO: exchange factory
        
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
