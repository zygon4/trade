
package com.zygon.data.feed;

import enterprise.db.data.Context;
import enterprise.db.data.Feed;

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
    
    @Override
    public String getDisplayIdentifier() {
        return this.name;
    }

    
}
