
package com.zygon.data.feed;

import com.zygon.data.Context;

/**
 *
 * @author david.charubini
 */
public abstract class CurrencyEventFeed<T> extends PollFeedAdapter<T> {

    private static final String TRADEABLE = "tradeable";
    private static final String CURRENCY = "currency";
    
    private final String tradeable;
    private final String currency;
    
    public CurrencyEventFeed(Context ctx, long cacheTime) {
        super(ctx, cacheTime);
        
        this.tradeable = ctx.getProperty(TRADEABLE);
        this.currency = ctx.getProperty(CURRENCY);
    }

    public final String getCurrency() {
        return currency;
    }

    public final String getTradeable() {
        return tradeable;
    }
}
