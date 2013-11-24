
package com.zygon.trade.modules.data;

import com.zygon.data.Context;
import com.zygon.data.EventFeed;
import com.zygon.data.EventFeed.Handler;
import com.zygon.data.FeedProvider;
import com.zygon.data.FeedProviderFactory;
import com.zygon.trade.Configuration;
import com.zygon.trade.Module;
import com.zygon.trade.Schema;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 *
 * @author zygon
 */
public class DataFeed extends Module {

    private static final Schema SCHEMA = new Schema("data_feed.json");
    
    private final FeedProvider feedProviderFactory = new FeedProviderFactory();
    private final Collection<Handler> feedRegistrations = new ArrayList<Handler>();
    private EventFeed feed = null;
    
    private String clazz = null;
    private String tradeable = null;
    private String currency = null;
    
    public DataFeed(String name, Schema schema) {
        super (name, schema);
    }
    
    public DataFeed(String name) {
        this (name, SCHEMA);
    }

    @Override
    public void configure(Configuration configuration) {
        
        // TBD: enable/disable configuration and handling already running feeds
        
        this.clazz = configuration.getValue("class");
        this.tradeable = configuration.getValue("tradeable");
        this.currency = configuration.getValue("currency");
    }
    
    protected Properties getDataFeedProperties(String cls, String tradeable, String currency) {
        Properties props = new Properties();
        props.setProperty(Context.PROP_CLS, cls);
        props.setProperty(Context.PROP_NAME, cls+":"+tradeable+"|"+currency);
        props.setProperty("tradeable", tradeable);
        props.setProperty("currency", currency);
        return props;
    }
    
    @Override
    public void initialize() {
        
        Context ctx = new Context(getDataFeedProperties(this.clazz, this.tradeable, this.currency));
        
        if (this.feed == null) {
            this.feed = (EventFeed<?>) this.feedProviderFactory.createFeed(ctx);
        }
        
        for (Handler<?> reg : this.feedRegistrations) {
            this.feed.register(reg);
        }
    }
    
    public void register (Handler reg) {
        this.feedRegistrations.add(reg);
    }

    @Override
    public void uninitialize() {
        for (Handler<?> reg : this.feedRegistrations) {
            this.feed.unregister(reg);
        }
    }
    
    public void unregister (Handler reg) {
        this.feedRegistrations.remove(reg);
    }
}
