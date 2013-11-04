
package com.zygon.trade.agent;

import com.zygon.trade.market.Message;
import java.util.Collection;

/**
 * 
 * @author david.charubini
 */
public interface SignalGenerator {
    public Collection<TradeSignal> getSignal(Message message);
}
