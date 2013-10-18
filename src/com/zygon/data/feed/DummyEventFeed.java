
package com.zygon.data.feed;

import enterprise.db.data.Context;

/**
 *
 * @author david.charubini
 */
public class DummyEventFeed extends PollFeedAdapter<String> {

    public DummyEventFeed(Context ctx) {
        super(ctx, 1000);
    }

    private int counter = 0;
    
    @Override
    public String get() {
        return ""+(this.counter++);
    }
}
