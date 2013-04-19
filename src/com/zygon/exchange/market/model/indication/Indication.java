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
    private final String id; // price, volume, etc
    private final long timestamp;

    public Indication(String tradableIdentifier, String id, long timestamp) {
        this.tradableIdentifier = tradableIdentifier;
        this.id = id;
        this.timestamp = timestamp;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getTradableIdentifier() {
        return this.tradableIdentifier;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    // This type of statement compilation should probably be in a separate
    // transformer
    public void getStatement(StringBuilder sb, String label) {
        sb.append(this.getClass().getSimpleName());
        sb.append('(');
        
        sb.append("tradableIdentifier='").append(this.getTradableIdentifier()).append('\'');
        sb.append(", id='").append(this.getId()).append('\'');
        sb.append(')');
        
        if (label != null) {
            sb.append(" as ").append(label);
        }
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
