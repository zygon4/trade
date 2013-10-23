
package com.zygon.trade.market.util;

import com.zygon.trade.market.data.Ticker;
import java.math.RoundingMode;

/**
 *
 * @author zygon
 */
public class TickerUtil {

    private TickerUtil() {}

    public static double getMidPrice(Ticker in) {
        return in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue();
    }
}
