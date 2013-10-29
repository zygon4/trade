
package com.zygon.trade.agent;

import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.trade.execution.ExecutionController;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.execution.simulation.SimulationBinding;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.interpret.TickerPriceInterpreter;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.numeric.Price;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
public class PriceAgent extends AbstractTickerAgent {

    private static Collection<Interpreter<Ticker>> getInterpreters() {
        Collection<Interpreter<Ticker>> interpreters = new ArrayList<>();
        
        interpreters.add(new TickerPriceInterpreter());
        
        return interpreters;
    }
    
    private static Strategy getStrategy() {
        
        Collection<Identifier> identifiers = new ArrayList<Identifier>(Arrays.asList(Price.ID));
        
        Strategy strategy = new Strategy(PriceAgent.class.getName()+"_Strategy", identifiers, null, null, 
                new ExecutionController(
                    new SimulationBinding("joe", new Wallet[]{new Wallet("USD", BigMoney.of(CurrencyUnit.USD, 1000.0))}, new MarketConditions("MtGox"))));
        
        return strategy;
    }
    
    public PriceAgent(String name) {
        super(name, getInterpreters(), getStrategy());
    }
}
