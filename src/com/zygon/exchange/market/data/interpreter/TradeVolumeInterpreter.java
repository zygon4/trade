/**
 * 
 */

package com.zygon.exchange.market.data.interpreter;

import com.zygon.exchange.market.data.DataHandler;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.exchange.market.Volume;

/**
 *
 * @author zygon
 */
public class TradeVolumeInterpreter implements DataHandler.Interpreter<Trade> {
    
    @Override
    public Volume interpret(Trade in) {
        return new Volume(in.getTradableIdentifier(), in.getTransactionCurrency(), in.getTradableAmount(), in.getTimestamp().getTime());
    }
}
