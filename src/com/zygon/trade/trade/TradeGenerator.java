
package com.zygon.trade.trade;

import com.zygon.trade.market.model.indication.Indication;
import java.util.Collection;

/**
 * 
 * @author david.charubini
 */
public interface TradeGenerator {
    public void notify(Indication message);
    public Collection<Trade> getTrades();
}
