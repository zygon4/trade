/**
 * 
 */

package com.zygon.trade.market.data.interpret;

import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.model.indication.numeric.Volume;

/**
 *
 * @author zygon
 */
public class TradeVolumeInterpreter implements Interpreter<Trade> {
    
    @Override
    public Volume[] interpret(Trade in) {
        return new Volume[] {
            new Volume(in.getCurrencyPair().baseSymbol, in.getCurrencyPair().counterSymbol, 
                in.getTimestamp().getTime(), in.getTradableAmount().doubleValue())};
    }
}
