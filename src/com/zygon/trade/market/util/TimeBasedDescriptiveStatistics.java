
package com.zygon.trade.market.util;

import java.util.Date;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author zygon
 */
public class TimeBasedDescriptiveStatistics extends DescriptiveStatistics {

    private final Date start;
    private Date lastViewedDate = null;
    
    public TimeBasedDescriptiveStatistics(Date start) {
        super();
        this.start = start;
    }
    
    public void addValue(double value, Date timestamp) {
        
        // Make sure the timestamp is within the boundaries of what we're 
        // interested in.
        if (timestamp.after(this.start)) {
            
            // Make sure the timestamp is strictly after the last
            if (this.lastViewedDate == null || timestamp.after(this.lastViewedDate)) {
                super.addValue(value);
                
                this.lastViewedDate = timestamp;
            } else {
                // This is a bit harsh - we could probably remove this in the
                // future.
                throw new IllegalArgumentException("Timestamp " + timestamp + " is after the last known timestamp " + this.lastViewedDate);
            }
        }
    }
}
