/**
 * 
 */

package com.zygon.exchange.market;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author zygon
 */
public class Volume {

    private final String tradableIdentifier;
    private final String transactionCurrency;
    private final double volume;
    private final BigDecimal bigVolumne;
    private final long timestamp;

    private Volume(String tradableIdentifier, String transactionCurrency, double volume, BigDecimal bigAmmount, long timestamp) {
        this.tradableIdentifier = tradableIdentifier;
        this.transactionCurrency = transactionCurrency;
        this.volume = volume;
        this.bigVolumne = bigAmmount;
        this.timestamp = timestamp;
    }

    public Volume(String tradableIdentifier, String transactionCurrency, double ammount, long timestamp) {
        this(tradableIdentifier, transactionCurrency, ammount, null, timestamp);
    }
    
    public Volume(String tradableIdentifier, String transactionCurrency, BigDecimal bigAmmount, long timestamp) {
        this(tradableIdentifier, transactionCurrency, bigAmmount.doubleValue(), timestamp);
    }
    
    public double getVolume() {
        // something is a little fishy about this.. i'd rather know up front what
        // i'll be returning here.. In fact, major decisions should be made 
        // about the usage of BigMoney.
        return this.bigVolumne != null ? this.bigVolumne.doubleValue() : this.volume;
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }

    public String getTradableIdentifier() {
        return this.tradableIdentifier;
    }

    public String getTransactionCurrency() {
        return this.transactionCurrency;
    }

    @Override
    public String toString() {
        return String.format("VOL[%s, %f, %s",this.getTransactionCurrency(), this.getVolume(), new Date(this.getTimestamp()));
    }
}
