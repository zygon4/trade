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
    public Aggregation getAggregation();
    public Classification getClassification();
    public String getID();
}
