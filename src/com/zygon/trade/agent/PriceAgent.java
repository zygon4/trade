
package com.zygon.trade.agent;

import com.zygon.trade.market.Message;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.interpret.TickerPriceInterpreter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    
    public PriceAgent(String name) {
        super(name, getInterpreters());
    }

    @Override
    protected void handle(List<Message> messages) {
        for (Message msg : messages) {
            System.out.println(msg);
        }
    }
}
