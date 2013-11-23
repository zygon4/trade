/**
 * 
 */

package com.zygon.trade.market.data;

import java.util.Date;

/**
 * This may or may not be required/good anymore
 *
 * @author zygon
 */
public class TradeableIndex {
    
    private final String identifer;
    private final Date ts;

    private String source;

    public TradeableIndex(String identifer, String source, Date ts) {
        this.identifer = identifer;
        this.source = source;
        this.ts = ts;
    }
    
    public TradeableIndex(String identifer, Date ts) {
        this(identifer, null, ts);
    }

    public String getIdentifer() {
        return this.identifer;
    }

    public String getSource() {
        return source;
    }

    public Date getTs() {
        return this.ts;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
