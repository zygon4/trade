/**
 * 
 */

package com.zygon.exchange.market;

import java.util.Date;
import org.joda.money.BigMoney;

/**
 *
 * @author zygon
 */
public class Price {
    
    private final String tradableIdentifier;
    private final double price;
    private final BigMoney bigPrice;
    private final long timestamp;

    private Price(String tradableIdentifier, BigMoney bigPrice, double price, long timestamp) {
        this.tradableIdentifier = tradableIdentifier;
        this.bigPrice = bigPrice;
        this.price = price;
        this.timestamp = timestamp;
    }
    
    public Price(String tradableIdentifier, BigMoney price, Date timestamp) {
        this(tradableIdentifier, price, price.getAmount().doubleValue(), timestamp.getTime());
    }

    public Price(String tradableIdentifier, double price, long timestamp) {
        this(tradableIdentifier, null, price, timestamp);
    }
    
    public double getPrice() {
        // something is a little fishy about this.. i'd rather know up front what
        // i'll be returning here.. In fact, major decisions should be made 
        // about the usage of BigMoney.
        return this.bigPrice != null ? this.bigPrice.getAmount().doubleValue() : this.price;
    }

    public String getTradableIdentifier() {
        return this.tradableIdentifier;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return String.format("PRICE[%s, %f, %s",this.getTradableIdentifier(), this.getPrice(), new Date(this.getTimestamp()));
    }
}
