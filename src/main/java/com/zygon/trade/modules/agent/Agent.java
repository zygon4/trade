
package com.zygon.trade.modules.agent;

import com.zygon.trade.Configuration;
import com.zygon.trade.Kernel;
import com.zygon.trade.Module;
import com.zygon.trade.Schema;
import com.zygon.trade.agent.AgentBuilder;
import com.zygon.trade.agent.trade.MACDTradeGenerator;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.TickerWriter;
import com.zygon.trade.market.data.interpret.RSIInterpreter;
import com.zygon.trade.market.data.interpret.TickerMACD;
import com.zygon.trade.market.util.Aggregation;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;
import com.zygon.trade.market.util.Duration;
import com.zygon.trade.market.util.Type;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.modules.execution.broker.Broker;
import com.zygon.trade.modules.execution.broker.BrokerModule;
import java.io.File;
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
        
        Aggregation leading = new Aggregation(Type.AVG, Duration._4, TimeUnit.HOURS);
        Aggregation lagging = new Aggregation(Type.AVG, Duration._24, TimeUnit.HOURS);
        Aggregation macd = new Aggregation(Type.AVG, Duration._1, TimeUnit.HOURS);
        interpreters.add(new TickerMACD(leading, lagging, macd));
        
        interpreters.add(new RSIInterpreter(new Aggregation(Type.AVG, Duration._4, TimeUnit.HOURS)));
        
        return interpreters;
    }
    
    private String getAgentDir(String agentName) {
        Kernel kernel = (Kernel) this.getModule(Kernel.ID);
        return kernel.getRootDirectory() + File.separator + "system" + File.separator + "data" + File.separator + "agent" + File.separator + agentName;
    }
    
    private static com.zygon.trade.agent.Agent<Ticker> buildAgent(String name, TickerWriter writer) {
        AgentBuilder<Ticker> builder = new AgentBuilder<Ticker>();
        builder.setName(name+"_agent");
        builder.setInterpreters(getInterpreters());
        builder.setSupportedIndicators(new ArrayList<Identifier>(Arrays.asList(MACDZeroCross.ID, MACDSignalCross.ID)));
        if (writer != null) {
            builder.setDataWriter(writer);
        }
        
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
        super.configure(configuration);
        // TBD: if the broker name changes.
        this.brokerName = configuration.getValue("broker");
        
        String agentName = configuration.getValue("name");
        String agentDataDirPath = this.getAgentDir(agentName);
        File agentDataDir = new File(agentDataDirPath);
        
        boolean directoryExists = agentDataDir.exists();
        
        if (!directoryExists) {
            directoryExists = agentDataDir.mkdirs();
        }
        
        TickerWriter writer = null;
        
        if (directoryExists) {
            writer = new TickerWriter(agentDataDirPath + File.separator + (agentName+ "_ticker.txt"));
        } else {
            this.getLogger().error("Error creating data dir " + agentDataDir.getAbsolutePath());
        }
        
        this.agent = buildAgent(agentName, writer);
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
