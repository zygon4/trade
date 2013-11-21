
package com.zygon.trade.modules.agent;

import com.zygon.trade.Configuration;
import com.zygon.trade.Module;
import com.zygon.trade.Schema;
import com.zygon.trade.agent.AgentBuilder;
import com.zygon.trade.agent.signal.MACDTradeGenerator;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.interpret.TickerMACD;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.modules.execution.broker.Broker;
import com.zygon.trade.modules.execution.broker.BrokerModule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class Agent extends Module {

    private static final Schema SCHEMA = new Schema("agent_schema.json");
    
    // TODO: use schema to generate appropriate interpretters, broker, and strategy engine
    // for each agent.  For now, we'll just hardcode this stuff.  This will likely
    // be the first complex schema and by the time we get a web interface up
    // and using a richer config - we'll probably evolve past the simple config
    // that I'm using now.  So, don't bother with hard lifting until we have
    // a better goal in mind.
    private static Collection<Interpreter<Ticker>> getInterpreters() {
        Collection<Interpreter<Ticker>> interpreters = new ArrayList<>();
        
        Aggregation leading = new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._15, TimeUnit.MINUTES);
        Aggregation lagging = new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._60, TimeUnit.MINUTES);
        Aggregation macd = new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._5, TimeUnit.MINUTES);
        interpreters.add(new TickerMACD(leading, lagging, macd));
        
        return interpreters;
    }
    
    private static com.zygon.trade.agent.Agent<Ticker> buildAgent(String name) {
        AgentBuilder<Ticker> builder = new AgentBuilder<Ticker>();
        builder.setName(name+"_agent");
        builder.setInterpreters(getInterpreters());
        builder.setSupportedIndicators(new ArrayList<Identifier>(Arrays.asList(MACDZeroCross.ID, MACDSignalCross.ID)));
        
        builder.setTradeGenerator(new MACDTradeGenerator());
        
        return builder.build();
    }
    
    private String brokerName = null;
    private com.zygon.trade.agent.Agent agent = null;
    
    public Agent(String name) {
        super (name); // TODO: schema
    }
    
    @Override
    public void configure(Configuration configuration) {
        // TBD: if the broker name changes.
        this.brokerName = configuration.getValue("broker");
        this.agent = buildAgent(configuration.getValue("name"));
    }
    
    @Override
    protected void doWriteStatus(StringBuilder sb) {
        sb.append(this.agent.getStrategySummary());
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
    
    @Override
    protected void unhook() {
        super.unhook();
        DataModule module = (DataModule) this.getModule(DataModule.ID);
        module.unregister(this.agent);
    }
    
    @Override
    public void uninitialize() {
        this.agent.uninitialize();
    }
}
