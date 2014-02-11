/**
 * 
 */

package com.zygon.trade.market.data.interpret;

import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.data.Ticker;

/**
 *
 * @author zygon
 */
/*pkg*/ abstract class TickerInterpreter implements Interpreter<Ticker> {
    public static final int HOURS_IN_DAY = 24;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MILLIS_IN_SECOND = 1000;
    public static final int NANOS_IN_MILLI = 1000;
}
