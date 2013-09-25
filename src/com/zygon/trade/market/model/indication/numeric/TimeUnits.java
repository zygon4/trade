/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 * 
 * 
 * Is this class used anymore?
 * 
 */
@Deprecated
enum TimeUnits {

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
    
    DAYS(TimeUnit.DAYS, "days"),
    HOURS(TimeUnit.HOURS, "hours"),
    MICROSECONDS(TimeUnit.MICROSECONDS, "micro"),
    MILLISECONDS(TimeUnit.MILLISECONDS, "msec"),
    MINUTES(TimeUnit.MINUTES, "min"),
    NANOSECONDS(TimeUnit.NANOSECONDS, "nano"),
    SECONDS(TimeUnit.SECONDS, "seconds");
    
    private final TimeUnit unit;
    private final String desc;

    private TimeUnits(TimeUnit unit, String desc) {
        this.unit = unit;
        this.desc = desc;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public String getDesc() {
        return desc;
    }
    
    public static TimeUnits instance (TimeUnit unit) {
        for (TimeUnits u : TimeUnits.values()) {
            if (unit == u.unit) {
                return u;
            }
        }
        
        return null;
    }
}
