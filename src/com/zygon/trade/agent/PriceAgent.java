
package com.zygon.trade.agent;

import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.interpret.TickerPriceInterpreter;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.numeric.Price;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author zygon
 */
@Deprecated
public class PriceAgent extends AbstractTickerAgent {

    private static Collection<Interpreter<Ticker>> getInterpreters() {
        Collection<Interpreter<Ticker>> interpreters = new ArrayList<>();
        
        interpreters.add(new TickerPriceInterpreter());
        
        return interpreters;
    }
    
    private static Strategy getStrategy() {
        
        Collection<Identifier> identifiers = new ArrayList<Identifier>(Arrays.asList(Price.ID));
        
        Strategy strategy = new Strategy(
                PriceAgent.class.getName()+"_Strategy", 
                identifiers, 
                null);
        
        return strategy;
    }
    
    public PriceAgent(String name) {
        super(name, getInterpreters(), getStrategy());
    }
}
