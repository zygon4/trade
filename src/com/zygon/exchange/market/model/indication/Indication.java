/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import java.util.Date;

/**
 *
 * @author zygon
 */
public class Indication {
    
    // TBD: How to handle other things such as: TransactionCurrency??  Use a map?
    
    private final String tradableIdentifier; // USD, BTC, etc
    private final Classification classification;
    private final long timestamp;

    // TBD: timestamp is provided to work with historic data.. we may want to
    // provide an alternative constructor which doesn't bother or provide a 
    // setter method.
    
    public Indication(String tradableIdentifier, Classification classification, long timestamp) {
        this.tradableIdentifier = tradableIdentifier;
        this.classification = classification;
        this.timestamp = timestamp;
    }
    
    // need to expose the classification?
    public String getId() {
        return this.classification.getId();
    }
    
    public String getTradableIdentifier() {
        return this.tradableIdentifier;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(this.getId());
        sb.append('[').append(this.getTradableIdentifier()).append(']').append(", ");
        sb.append(new Date(this.getTimestamp()));
        
        return sb.toString();
    }
}
