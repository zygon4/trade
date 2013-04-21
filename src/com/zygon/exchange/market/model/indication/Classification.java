/**
 * 
 */

package com.zygon.exchange.market.model.indication;

/**
 *
 * @author zygon
 */
public enum Classification {

    PRICE ("price"),
    VOLUME ("volume"),
    SPREAD ("spread"),
    DEPTH ("depth");
    
    private final String id;
    
    private Classification(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
