/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.trade.market.util;

// better name??

public enum Type {
    HIGH("max"), 
    LOW("min"), 
    AVG("avg");

    private Type(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }
    private final String val;
    
}
