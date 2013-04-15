/**
 * 
 */

package com.zygon.exchange.market.model.indication.technical;

import com.zygon.exchange.market.Price;
import com.zygon.exchange.market.Volume;

/**
 *
 * @author zygon
 */
public enum Classification {

    PRICE ("price", Price.class.getName()),
    VOLUME ("volume", Volume.class.getName()),
    DEPTH ("depth", null);
    
    private final String esperToken;
    private final String className;

    private Classification(String esperToken, String className) {
        this.esperToken = esperToken;
        this.className = className;
    }
    
    public String getClassName() {
        return className;
    }

    public String getEsperToken() {
        return esperToken;
    }
}
