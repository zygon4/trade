/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

import com.zygon.trade.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class Support extends SimpleMarketIndication {

    public Support(String tradableIdentifier, long timestamp, double value) {
        super(MarketIndication.IDS.SUPPORT, tradableIdentifier, Classification.PRICE, timestamp, value);
    }
}
