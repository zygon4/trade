/**
 * 
 */

package com.zygon.exchange.market.data.handler;

import com.zygon.exchange.market.data.DataHandler;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.exchange.market.Volume;

/**
 *
 * @author zygon
 */
public class TradeVolumeHandler extends DataHandler<Trade, Object> {
    
    public TradeVolumeHandler(String name) {
        super(name);
    }
    
    @Override
    protected Volume translate(Trade in) {
        return new Volume(in.getTradableIdentifier(), in.getTransactionCurrency(), in.getTradableAmount(), in.getTimestamp().getTime());
    }
}
