/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

import com.zygon.trade.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class Resistance extends SimpleMarketIndication {

    public Resistance(String tradableIdentifier, long timestamp, double level) {
        super(MarketIndication.IDS.RESISTANCE, tradableIdentifier, Classification.PRICE, timestamp, level);
    }
}
