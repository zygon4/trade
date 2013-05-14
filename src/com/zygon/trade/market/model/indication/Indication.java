/**
 * 
 */

package com.zygon.trade.market.model.indication;

import com.zygon.trade.market.Message;
import java.util.Date;

/**
 *
 * @author zygon
 */
public class Indication extends Message {
    
    // TBD: How to handle other things such as: TransactionCurrency??  Use a map?
    
    private final Identifier id;
    private final String tradableIdentifier; // USD, BTC, etc
    private final Classification classification;
    private final long timestamp;

    // TBD: timestamp is provided to work with historic data.. we may want to
    // provide an alternative constructor which doesn't bother or provide a 
    // setter method.
    
    public Indication(Identifier id, String tradableIdentifier, Classification classification, long timestamp) {
        this.id = id;
        this.tradableIdentifier = tradableIdentifier;
        this.classification = classification;
        this.timestamp = timestamp;
    }

    public Classification getClassification() {
        return this.classification;
    }

    public final Identifier getId() {
        return this.id;
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
