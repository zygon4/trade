/**
 * 
 */

package com.zygon.trade.market.model.indication;

import com.zygon.trade.market.util.Classification;
import java.util.Date;

/**
 *
 * @author zygon
 */
public class Indication {
    
    private final Identifier id;
    private final String tradableIdentifier; // USD, BTC, etc
    private final long timestamp;

    // TBD: timestamp is provided to work with historic data.. we may want to
    // provide an alternative constructor which doesn't bother or provide a 
    // setter method.
    
    public Indication(Identifier id, String tradableIdentifier, long timestamp) {
        if (id == null || tradableIdentifier == null) {
            throw new IllegalArgumentException("No null arguments permitted");
        }
        this.id = id;
        this.tradableIdentifier = tradableIdentifier;
        this.timestamp = timestamp;
    }

    public Classification getClassification() {
        return this.id.getClassification();
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
