/**
 * 
 */

package com.zygon.trade.strategy;

import com.xeiam.xchange.dto.Order;
import java.math.BigDecimal;

/**
 *
 * The intent for this class is a way to frame an active Trade.  A collection of
 * trades could be (for instance) cropped up waiting for the respective exit
 * points.
 * 
 * @author zygon
 * 
 * Version 1.0 - subject to a lot of change.
 */
public class Trade {

    private final String currency;
    private final String tradeableIdentifier;
    private final Order.OrderType type;
    private final BigDecimal entryPoint;
    private final BigDecimal exitPoint;

    // TBD: exit point classification/rules? e.g. exit is "double entry"
    
    public Trade(String currency, String tradeableIdentifier, Order.OrderType type,
            BigDecimal entryPoint, BigDecimal exitPoint) {
        
        if (currency == null || tradeableIdentifier == null || type == null) {
            throw new IllegalArgumentException("Must provide currency, tradeableIdentifier, and type");
        }
        
        // check if the entry/exits are sane versus the bid/ask??
        
        this.currency = currency;
        this.tradeableIdentifier = tradeableIdentifier;
        this.type = type;
        this.entryPoint = entryPoint; // Can be null implying a market order
        this.exitPoint = exitPoint; // Can be null implying no assumed exit
    }
    
    public Trade(String currency, String tradeableIdentifier, Order.OrderType type) {
        this (currency, tradeableIdentifier, type, null, null);
    }

    public final String getCurrency() {
        return this.currency;
    }

    public BigDecimal getEntryPoint() {
        return this.entryPoint;
    }

    public BigDecimal getExitPoint() {
        return this.exitPoint;
    }

    public final String getTradeableIdentifier() {
        return this.tradeableIdentifier;
    }

    public final Order.OrderType getType() {
        return this.type;
    }
}