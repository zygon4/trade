
package com.zygon.trade.market.data;

import com.google.common.collect.Lists;
import com.zygon.trade.market.data.interpret.TickerPriceInterpreter;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class TickerContext extends DataSource<Ticker> {
    
    private static Collection<Interpreter<Ticker>> createDefaultInterpreters() {
        Collection<Interpreter<Ticker>> interpreters = Lists.newArrayList();
        interpreters.add(new TickerPriceInterpreter(true));
        // TODO: spread, etc
        return interpreters;
    }
    
    private static Collection<Interpreter<Ticker>> getInterpreters(Collection<Interpreter<Ticker>> interpreters) {
        
        interpreters.addAll(createDefaultInterpreters());
        return interpreters;
        
//        Aggregation leading = new Aggregation(Type.AVG, Duration._4, TimeUnit.HOURS);
//        Aggregation lagging = new Aggregation(Type.AVG, Duration._24, TimeUnit.HOURS);
//        Aggregation macd = new Aggregation(Type.AVG, Duration._1, TimeUnit.HOURS);
//        interpreters.add(new TickerMACD(leading, lagging, macd));
//        
//        interpreters.add(new RSIInterpreter(new Aggregation(Type.AVG, Duration._4, TimeUnit.HOURS)));
    }
    
    public TickerContext(Collection<Interpreter<Ticker>> interpreters, String exchangeIdentifier) {
        super(getInterpreters(interpreters), "TICKER"+exchangeIdentifier);
    }

    
}
