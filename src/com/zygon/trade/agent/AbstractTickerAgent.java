
package com.zygon.trade.agent;

import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.data.Ticker;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public abstract class AbstractTickerAgent extends Agent<Ticker> {

    public AbstractTickerAgent(String name, Collection<Interpreter<Ticker>> interpreters) {
        super(name, interpreters);
    }
}