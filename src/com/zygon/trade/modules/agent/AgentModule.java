
package com.zygon.trade.modules.agent;

import com.zygon.trade.Module;
import com.zygon.trade.ParentModule;
import com.zygon.trade.agent.AgentBuilder;
import com.zygon.trade.agent.signal.MACDTradeGenerator;
import com.zygon.trade.execution.exchange.mtgox.MtGoxExchange;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.interpret.TickerMACD;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class AgentModule extends ParentModule {

    private static com.zygon.trade.agent.Agent<Ticker> buildAgent(String name) {
        AgentBuilder<Ticker> builder = new AgentBuilder<Ticker>();
        builder.setName(name+"_agent");
        builder.setExchange(new MtGoxExchange());
        builder.setInterpreters(getInterpreters());
        builder.setSupportedIndicators(new ArrayList<Identifier>(Arrays.asList(MACDZeroCross.ID, MACDSignalCross.ID)));
        
        builder.setTradeGenerator(new MACDTradeGenerator());
        
        return builder.build();
    }
    
    // TBD: this child should be the product of a create command
    private final Agent agent = new Agent("a");
    
    public AgentModule(String name) {
        super(name, Agent.class);
        
        this.agent.setAgent(buildAgent(name));
    }

    @Override
    public Module[] getModules() {
        return new Module[] {
            this.agent
        };
    }
    
    private static Collection<Interpreter<Ticker>> getInterpreters() {
        Collection<Interpreter<Ticker>> interpreters = new ArrayList<>();
        
        Aggregation leading = new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._15, TimeUnit.MINUTES);
        Aggregation lagging = new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._60, TimeUnit.MINUTES);
        Aggregation macd = new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._5, TimeUnit.MINUTES);
        interpreters.add(new TickerMACD(leading, lagging, macd));
        
        return interpreters;
    }
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }
}
