
package com.zygon.trade.modules.agent;

import com.zygon.trade.Configuration;
import com.zygon.trade.Module;
import com.zygon.trade.Schema;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.modules.execution.broker.Broker;
import com.zygon.trade.modules.execution.broker.BrokerModule;
import com.zygon.trade.trade.TradeBroker;

/**
 *
 * @author zygon
 */
public class Agent extends Module {

    private static final Schema SCHEMA = new Schema("agent_schema.json");
    
    private String brokerName = null;
    private com.zygon.trade.agent.Agent agent = null;
    
    public Agent(String name) {
        super (name); // TODO: schema
    }
    
    @Override
    public void configure(Configuration configuration) {
        super.configure(configuration); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected void doWriteStatus(StringBuilder sb) {
        sb.append(this.agent.getStrategySummary());
    }
    
    @Override
    public Module[] getModules() {
        return null;
    }

    @Override
    protected void hook() {
        super.hook();
        DataModule module = (DataModule) this.getModule(DataModule.ID);
        module.register(this.agent);
        
        BrokerModule brokerModule = (BrokerModule) this.getModule(BrokerModule.ID);
        Broker broker = brokerModule.getBroker(this.brokerName);
        
        if (broker == null) {
            this.getLogger().debug("Unable to find broker: " + this.brokerName);
        } else {
            this.agent.setBroker(broker.getBroker());
        }
    }

    /*pkg*/ com.zygon.trade.agent.Agent getAgent() {
        return this.agent;
    }
    
    @Override
    public void initialize() {
        this.agent.initialize();
    }
    
    /*pkg*/ void setBroker (TradeBroker broker) {
        this.agent.setBroker(broker);
    }
    
    @Override
    protected void unhook() {
        super.unhook();
        DataModule module = (DataModule) this.getModule("data");
        module.unregister(this.agent);
    }
    
    @Override
    public void uninitialize() {
        this.agent.uninitialize();
    }

    void setAgent(com.zygon.trade.agent.Agent<Ticker> agent) {
        this.agent = agent;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }
}
