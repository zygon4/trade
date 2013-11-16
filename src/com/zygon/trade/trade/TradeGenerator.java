
package com.zygon.trade.trade;

import com.zygon.trade.market.Message;
import java.util.Collection;

/**
 * 
 * @author david.charubini
 */
public interface TradeGenerator {
    public void notify(Message message);
    public Collection<Trade> getTrades();
}
