/**
 * 
 */

package com.zygon.trade.market.model.indication;

import com.zygon.trade.market.util.Classification;

/**
 *
 * @author zygon
 */
public interface Identifier {
    public boolean equals (Identifier id);
    public Classification getClassification();
    public String getID();
}
