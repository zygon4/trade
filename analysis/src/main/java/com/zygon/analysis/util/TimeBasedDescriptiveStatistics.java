
package com.zygon.analysis.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author zygon
 */
public class TimeBasedDescriptiveStatistics extends DescriptiveStatistics {

    /**
     * Returns the Date which represents the earliest allowable time in the
     * moving average calculation.
     *
     * @param duration
     * @param timeUnit
     * @return
     */
    private static Date calculateStartDate(Duration duration, TimeUnit timeUnit) {

        Calendar cal = Calendar.getInstance();

        switch (timeUnit) {
            case DAYS:
                cal.add(Calendar.DATE, -duration.getVal());
                break;
            case HOURS:
                cal.add(Calendar.HOUR, -duration.getVal());
                break;
            case MILLISECONDS:
                cal.add(Calendar.MILLISECOND, -duration.getVal());
                break;
            case MINUTES:
                cal.add(Calendar.MINUTE, -duration.getVal());
                break;
            case SECONDS:
                cal.add(Calendar.SECOND, -duration.getVal());
                break;
            default:
                throw new UnsupportedOperationException(timeUnit.name());
        }

        return new Date(cal.getTimeInMillis());
    }

    private final TreeMap<Date,Double> values = new TreeMap<Date,Double>();
    private final Duration duration;
    private final TimeUnit timeUnits;

    public TimeBasedDescriptiveStatistics(Duration duration, TimeUnit timeUnits) {
        super();
        this.duration = duration;
        this.timeUnits = timeUnits;
    }

    public void addValue(double value, Date timestamp) {

        synchronized (this.values) {
            this.addValue(value);
            this.values.put(timestamp, value);

            Date cutoff = calculateStartDate(this.duration, this.timeUnits);
            Set<Date> olderThanKeys = this.values.headMap(cutoff).keySet();

            if (!olderThanKeys.isEmpty()) {
                // to avoid CME we (as I see it now) need to make copy of the
                // removed keys (which, to be fair, should not be many), and
                // then use these keys to remove values from the original
                // collection

                Set<Date> keys = new HashSet<Date>();
                for (Date olderThanKey : olderThanKeys) {
                    keys.add(new Date(olderThanKey.getTime()));
                }

                for (Date key : keys) {
                    this.values.remove(key);
                }

                // This type of re-copy might be too expensive but we'll see
                DescriptiveStatistics newStats = new DescriptiveStatistics(ArrayUtils.toPrimitive(this.values.values().toArray(new Double[this.values.size()])));

                DescriptiveStatistics.copy(newStats, this);
            }
        }
    }
}
