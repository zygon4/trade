/**
 * 
 */

package com.zygon.exchange.market.model.indication.technical;

import com.zygon.exchange.market.model.indication.Indication;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class AveragePrice extends Indication {

//    private static final String STATEMENT_FMT =
//            "expression midPrice { x => (x.bid + x.ask) / 2 } " +
//            "select avg(tick.last) " +
//            "from SMA(security='%s').win:ext_timed(timestamp, %d seconds) as tick";
    
    /*
     * year-part : (number|variable_name) ("years" | "year")
month-part : (number|variable_name) ("months" | "month")
week-part : (number|variable_name) ("weeks" | "week")
day-part : (number|variable_name) ("days" | "day")
hour-part : (number|variable_name) ("hours" | "hour")
minute-part : (number|variable_name) ("minutes" | "minute" | "min")
seconds-part : (number|variable_name) ("seconds" | "second" | "sec")
milliseconds-part : (number|variable_name) ("milliseconds" | "millisecond" | "msec")
     */
    
    private static String toEsper (TimeUnit units) {
        String unit = null;
        
        switch (units) {
            case DAYS:
                unit = "days";
                break;
            case HOURS:
                unit = "hours";
                break;
            case MICROSECONDS:
                throw new UnsupportedOperationException();
            case MILLISECONDS:
                unit = "msec";
                break;
            case MINUTES:
                unit = "min";
                break;
            case NANOSECONDS:
                throw new UnsupportedOperationException();
            case SECONDS:
                unit = "seconds";
                break;
        }
        
        return unit;
    }
    
    private static String getName(long duration, TimeUnit units) {
        return "AVG_PRICE_"+duration+"_"+units.toString();
    }
    
    private static String STMT_FMT = "select avg(price) "
            + "from %s(transactionCurrency='%s').win:ext_timed(timestamp, %d %s)";
    
    public AveragePrice (String security, long duration, TimeUnit units) {
        super(getName(duration, units), 
                getName(duration, units),//              Price.class.getSimpleName(), 
              Price.class.getName(), 
              String.format(STMT_FMT, getName(duration, units), security, duration, toEsper(units)));
    }
}
