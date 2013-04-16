/**
 * 
 */

package com.zygon.exchange.market.data.interpreter;

import com.xeiam.xchange.dto.marketdata.Ticker;
import com.zygon.exchange.market.Price;
import com.zygon.exchange.market.data.DataHandler;
import java.math.RoundingMode;

/**
 *
 * @author zygon
 */
public class TickerPriceInterpreter implements DataHandler.Interpreter<Ticker> {

    @Override
    public Price interpret(Ticker in) {
        return new Price(in.getTradableIdentifier(), in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP), in.getTimestamp());
    }
}
