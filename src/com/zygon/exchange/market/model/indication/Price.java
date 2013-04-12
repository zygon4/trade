/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import java.util.Date;
import org.joda.money.BigMoney;

/**
 *
 * @author zygon
 */
public class Price {
    
    private final String transactionCurrency;
    private final double price;
    private final BigMoney bigPrice;
    private final long timestamp;

    private Price(String transactionCurrency, BigMoney bigPrice, double price, long timestamp) {
        this.transactionCurrency = transactionCurrency;
        this.bigPrice = bigPrice;
        this.price = price;
        this.timestamp = timestamp;
    }
    
    Price(String transactionCurrency, BigMoney price, Date timestamp) {
        this(transactionCurrency, price, price.getAmount().doubleValue(), timestamp.getTime());
    }

    public Price(String transactionCurrency, double price, long timestamp) {
        this(transactionCurrency, null, price, timestamp);
    }
    
    public double getPrice() {
        // something is a little fishy about this.. i'd rather know up front what
        // i'll be returning here.. In fact, major decisions should be made 
        // about the usage of BigMoney.
        return this.bigPrice != null ? this.bigPrice.getAmount().doubleValue() : this.price;
    }

    public String getTransactionCurrency() {
        return this.transactionCurrency;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}
