
package com.zygon.trade.agent;

import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.execution.simulation.SimulationBinding;
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
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
@Deprecated
public class SimpleMAAgent extends AbstractTickerAgent {

    private static Collection<Interpreter<Ticker>> getInterpreters() {
        Collection<Interpreter<Ticker>> interpreters = new ArrayList<>();
        
        Aggregation leading = new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._15, TimeUnit.MINUTES);
        Aggregation lagging = new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._60, TimeUnit.MINUTES);
        Aggregation macd = new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._5, TimeUnit.MINUTES);
        interpreters.add(new TickerMACD(leading, lagging, macd));
        
        return interpreters;
    }
    
    private static Strategy getStrategy() {
        Collection<Identifier> identifiers = new ArrayList<Identifier>(Arrays.asList(MACDZeroCross.ID, MACDSignalCross.ID));
        
        Strategy strategy = new Strategy(
                PriceAgent.class.getName()+"_Strategy", 
                identifiers, 
                null, 
                new SignalHandler(new ExecutionController(
                    new SimulationBinding("joe", new Wallet[]{new Wallet("USD", BigMoney.of(CurrencyUnit.USD, 1000.0))}, new MarketConditions("MtGox")))));
        
        return strategy;
    }
    
    public SimpleMAAgent(String name) {
        super(name, getInterpreters(), getStrategy());
    }
}
