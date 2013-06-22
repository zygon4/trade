/**
 * 
 */

package com.zygon.trade.market.data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author zygon
 */
@Embeddable
public class TradeableIndex implements Serializable {
    
    @Column(name="identifier")
    private String identifer;
    
    @Column(name="ts")
    private long ts;

    public String getIdentifer() {
        return this.identifer;
    }

    public long getTs() {
        return this.ts;
    }

    public void setIdentifer(String identifer) {
        this.identifer = identifer;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}
