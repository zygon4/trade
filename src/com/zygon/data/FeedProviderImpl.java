
package com.zygon.data;

import java.util.Properties;

/**
 *
 * @author david.charubini
 */
public class FeedProviderImpl implements FeedProvider {

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
    
    public static void main(String[] args) throws InterruptedException {
        FeedProvider fp = new FeedProviderImpl();
        
        Properties props = new Properties();
        props.setProperty(Context.PROP_CLS, "enterprise.db.data.feed.DummyFeed");
        props.setProperty(Context.PROP_NAME, "DummyFeed");
        Context ctx = new Context(props);
        
        Feed<String> createFeed = fp.createFeed(ctx);
        PollFeed<String> feed = (PollFeed<String>) createFeed;
        
        for (int i = 0; i < 10; i++) {
            System.out.println(feed.get());
        }
        
        
        
        props.clear();
        props.setProperty(Context.PROP_CLS, "enterprise.db.data.feed.DummyEventFeed");
        props.setProperty(Context.PROP_NAME, "DummyEventFeed");
        ctx = new Context(props);
        
        createFeed = fp.createFeed(ctx);
        EventFeed<String> pushFeed = (EventFeed<String>) createFeed;
        
        EventFeed.Registration<String> reg = new EventFeed.Registration<String>() {

            @Override
            public void handle(String r) {
                System.out.println(r);
            }
        };
        
        pushFeed.register(reg);
        
        Thread.sleep (10000);
        
        pushFeed.unregister(reg);
        
        Thread.sleep (5000);
    }
    //"com.xeiam.xchange.mtgox.v2.MtGoxExchange"
}
