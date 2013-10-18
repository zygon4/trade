
package com.zygon.data.feed;

import enterprise.db.data.Context;
import enterprise.db.data.EventFeed;
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

    private final Set<Registration<T>> registrations = new HashSet<Registration<T>>();

    protected final Set<Registration<T>> getRegistrations() {
        return this.registrations;
    }
    
    @Override
    public void register(Registration<T> reg) {
        this.registrations.add(reg);
    }

    @Override
    public void unregister(Registration<T> reg) {
        this.registrations.remove(reg);
    }
}
