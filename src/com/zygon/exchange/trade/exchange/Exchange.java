/**
 * 
 */

package com.zygon.exchange.trade.exchange;

import com.xeiam.xchange.dto.trade.MarketOrder;
import com.zygon.exchange.trade.FeedHandler;
import com.zygon.exchange.trade.FeedProvider;

/**
 *
 * @author zygon
 */
public interface Exchange<FEED_TYPE> {
    
    public String getName();
    /*
     * TBD: a collection of feed distributors
     */
    public FeedHandler<FEED_TYPE> getFeedDistributor();
    /*
     * TBD: a collection of feed distributors
     */
    public FeedProvider<FEED_TYPE> getFeedProvider();
    
    public void placeMarketOrder (MarketOrder order);
}
