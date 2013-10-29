
package com.zygon.trade.agent;

import com.zygon.trade.market.Message;

/**
 *
 * @author david.charubini
 */
public interface EventCriteria {
    public TradeSignal getSignal(Message message);
}
