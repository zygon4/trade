/**
 * 
 */

package com.zygon.trade.market.model.indication;

/**
 *
 * @author zygon
 */
public interface Identifier {
    public boolean equals (Identifier id);
    public Classification getClassification();
    public String getID();
}
