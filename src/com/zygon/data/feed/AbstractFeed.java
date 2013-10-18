
package com.zygon.data.feed;

import com.zygon.data.Context;
import com.zygon.data.Feed;

/**
 *
 * @author david.charubini
 */
public abstract class AbstractFeed<T> implements Feed<T> {

    private final Context ctx;
    private final String name;

    public AbstractFeed(Context ctx) {
        this.ctx = ctx;
        this.name = ctx.getName();
    }

    public Context getCtx() {
        return ctx;
    }
    
    @Override
    public String getDisplayIdentifier() {
        return this.name;
    }
}
