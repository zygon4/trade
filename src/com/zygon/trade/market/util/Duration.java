/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.trade.market.util;

/**
 *
 * @author zygon
 */
public enum Duration {
    _1(1), 
    _4(4), 
    _5(5), 
    _15(15), 
    _30(30), 
    _60(60), 
    _24(24);
    
    private final int val;

    private Duration(int val) {
        this.val = val;
    }

    public int getVal() {
        return this.val;
    }
    
}
