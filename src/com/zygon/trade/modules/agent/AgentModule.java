
package com.zygon.trade.modules.agent;

import com.zygon.trade.Module;
import com.zygon.trade.ParentModule;
import com.zygon.trade.agent.AgentBuilder;
import com.zygon.trade.agent.signal.MACDTradeGenerator;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.interpret.TickerMACD;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;
import com.zygon.trade.trade.TradeSummary;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class AgentModule extends ParentModule {

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
    
    private final Agent agent = new Agent("macd");
    {
        agent.setAgent(buildAgent(agent.getDisplayname()));
        agent.setBrokerName("mtgox");
    }
    // TBD: this child should be the product of a create command
    private final Agent[] agents = new Agent[] {agent};
    
    public AgentModule(String name) {
        super(name, Agent.class);
    }

    @Override
    protected void doWriteStatus(StringBuilder sb) {
        for (Agent agent : this.agents) {
            sb.append(agent.getDisplayname()).append(": ").append(agent.getAgent().getStrategySummary()).append('\n');
        }
    }
    
    @Override
    public Module[] getModules() {
        return this.agents;
    }
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }
}
