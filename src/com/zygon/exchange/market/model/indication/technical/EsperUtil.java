/**
 * 
 */

package com.zygon.exchange.market.model.indication.technical;

import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 *
 * @author zygon
 * 
 * pkg for now
 */
/*pkg*/ class EsperUtil {

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
    
    
    public static String toEsper (TimeUnit units) {
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
}
