/**
 * 
 */

package com.zygon.trade.strategy;

/**
 *
 * @author zygon
 */
@Deprecated
public class ExitStatus {
    
    public static enum ExitReason {
        STOP_LOSS,
        TAKE_PROFIT;
    }
    
    public final boolean atExitPoint;
    public final ExitReason reason;

    public ExitStatus(boolean atExitPoint, ExitReason reason) {
        this.atExitPoint = atExitPoint;
        this.reason = reason;
    }

    public ExitStatus(boolean atExitPoint) {
        this(atExitPoint, null);
    }

    public ExitReason getReason() {
        return this.reason;
    }

    public boolean isAtExitPoint() {
        return this.atExitPoint;
    }
}
