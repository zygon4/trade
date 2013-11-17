
package com.zygon.trade.modules.data;

import com.zygon.data.Context;
import com.zygon.data.EventFeed;
import com.zygon.data.EventFeed.Handler;
import com.zygon.data.FeedProvider;
import com.zygon.data.FeedProviderImpl;
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

    private static Properties getProps(String cls, String tradeable, String currency) {
        Properties props = new Properties();
        props.setProperty(Context.PROP_CLS, cls);
        props.setProperty(Context.PROP_NAME, cls+":"+tradeable+"|"+currency);
        props.setProperty("tradeable", tradeable);
        props.setProperty("currency", currency);
        return props;
    }
    
    private static final Schema SCHEMA = new Schema("data_feed.json");
    
    private final FeedProvider feedProvider = new FeedProviderImpl();
    private final Collection<Handler> feedRegistrations = new ArrayList<Handler>();
    private EventFeed feed = null;
    
    public DataFeed(String name) {
        super(name, SCHEMA);
    }

    @Override
    public Module[] getModules() {
        return null;
    }

    @Override
    public void initialize() {
        Configuration config = this.getConfiguration();
        
        Context ctx = new Context(getProps(config.getValue("class"), config.getValue("tradeable"), config.getValue("currency")));
        
        if (this.feed == null) {
            this.feed = (EventFeed<?>) this.feedProvider.createFeed(ctx);
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
