
package com.zygon.data;

/**
 *
 * @author david.charubini
 */
public class FeedProviderFactory implements FeedProvider {

    @Override
    public <T> Feed<T> createFeed(Context ctx) {
        String cls = ctx.getClazz();
        
        Feed<T> feed = null;
        try {
            feed = (Feed<T>) Class.forName(cls).getConstructor(Context.class).newInstance(ctx);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return feed;
    }
}
