/**
 * 
 */

package com.zygon.trade.market.data;

/**
 * This may or may not be required/good anymore
 *
 * @author zygon
 */
public class TradeableIndex {
    
    private final String identifer;
    private final long ts;

    private String source;

    public TradeableIndex(String identifer, String source, long ts) {
        this.identifer = identifer;
        this.source = source;
        this.ts = ts;
    }
    
    public TradeableIndex(String identifer, long ts) {
        this(identifer, null, ts);
    }

    public String getIdentifer() {
        return this.identifer;
    }

    public String getSource() {
        return source;
    }

    public long getTs() {
        return this.ts;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
