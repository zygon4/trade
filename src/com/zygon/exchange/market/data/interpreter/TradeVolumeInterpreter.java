/**
 * 
 */

package com.zygon.exchange.market.data.interpreter;

import com.zygon.exchange.market.data.DataHandler;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.exchange.market.model.indication.numeric.Numeric;
import com.zygon.exchange.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class TradeVolumeInterpreter implements DataHandler.Interpreter<Trade> {
    
    //in.getTransactionCurrency() ???
    
    @Override
    public Numeric interpret(Trade in) {
        return new Numeric(in.getTradableIdentifier(), Classification.VOLUME.getId(), 
                in.getTimestamp().getTime(), in.getTradableAmount().doubleValue());
    }
}
