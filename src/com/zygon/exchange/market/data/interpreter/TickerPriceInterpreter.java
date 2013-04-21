/**
 * 
 */

package com.zygon.exchange.market.data.interpreter;

import com.xeiam.xchange.dto.marketdata.Ticker;
import com.zygon.exchange.market.model.indication.numeric.Numeric;
import com.zygon.exchange.market.data.DataHandler;
import com.zygon.exchange.market.model.indication.Classification;
import java.math.RoundingMode;

/**
 *
 * @author zygon
 */
public class TickerPriceInterpreter implements DataHandler.Interpreter<Ticker> {

    @Override
    public Numeric interpret(Ticker in) {
        return new Numeric(in.getTradableIdentifier(), Classification.PRICE.getId(), in.getTimestamp().getTime(),
                in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue());
    }
}
