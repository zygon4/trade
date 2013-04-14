/**
 * 
 */

package com.zygon.exchange.market.data.handler;

import com.zygon.exchange.market.data.DataHandler;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.exchange.InformationHandler;
import com.zygon.exchange.market.Volume;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class TradeVolumeHandler extends DataHandler<Trade, Object> {
    
    public TradeVolumeHandler(String name, Collection<InformationHandler<Object>> targets) {
        super(name, targets);
    }
    
    @Override
    protected Volume translate(Trade in) {
        return new Volume(in.getTradableIdentifier(), in.getTransactionCurrency(), in.getTradableAmount(), in.getTimestamp().getTime());
    }
}
