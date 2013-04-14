/*
 *
 */
package com.zygon.exchange.market.model.indication.technical;

/**
 *
 * @author zygon
 */
public enum Aggregation {
    HIGH("max"), 
    LOW("min"), 
    AVG("avg");

    private Aggregation(String esperToken) {
        this.esperToken = esperToken;
    }

    /*pkg*/ String getEsperToken() {
        return this.esperToken;
    }
    
    private final String esperToken;
}
