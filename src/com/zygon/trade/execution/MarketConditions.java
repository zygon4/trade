/**
 * 
 */

package com.zygon.trade.execution;

import java.math.BigDecimal;

/**
 *
 * Just a pojo for now with some properties.
 * 
 * @author zygon
 */
public final class MarketConditions {
    
    private final String tradeableIdentifier;
    private final BigDecimal price;
    private final String currency;

    public MarketConditions(String tradeableIdentifier, BigDecimal price, String currency) {
        this.tradeableIdentifier = tradeableIdentifier;
        this.price = price;
        this.currency = currency;
    }

    public String getCurrency() {
        return this.currency;
    }
    
    public BigDecimal getPrice() {
        return this.price;
    }

    public String getTradeableIdentifier() {
        return this.tradeableIdentifier;
    }
}
