
package com.zygon.data.feed.currency;

import com.zygon.data.Context;
import com.zygon.data.feed.PollFeedAdapter;

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
        
        this.tradeable = ctx.getProperties().getProperty(TRADEABLE);
        this.currency = ctx.getProperties().getProperty(CURRENCY);
    }

    public final String getCurrency() {
        return currency;
    }

    public final String getTradeable() {
        return tradeable;
    }
}
