
package com.zygon.data.feed;

import com.zygon.data.Context;
import com.zygon.data.EventFeed;
import com.zygon.data.EventFeed.Handler;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author david.charubini
 */
public abstract class AbstractEventFeed<T> extends AbstractFeed<T> implements EventFeed<T> {

    public AbstractEventFeed(Context ctx) {
        super(ctx);
    }

    private final Set<Handler<T>> registrations = new HashSet<Handler<T>>();

    protected final Set<Handler<T>> getHandlers() {
        return this.registrations;
    }
    
    @Override
    public void register(Handler<T> reg) {
        this.registrations.add(reg);
    }

    @Override
    public void unregister(Handler<T> reg) {
        this.registrations.remove(reg);
    }
}
