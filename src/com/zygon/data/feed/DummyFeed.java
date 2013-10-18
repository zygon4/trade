
package com.zygon.data.feed;

import enterprise.db.data.Context;
import enterprise.db.data.PollFeed;

/**
 *
 * @author david.charubini
 */
public class DummyFeed extends AbstractFeed<String> implements PollFeed<String> {

    public DummyFeed(Context ctx) {
        super(ctx);
    }
    
    @Override
    public String getDisplayIdentifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private int counter = 0;
    
    @Override
    public String get() {
        return ""+(this.counter++);
    }
}
