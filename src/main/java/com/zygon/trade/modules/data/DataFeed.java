
package com.zygon.trade.modules.data;

import com.google.common.collect.Lists;
import com.zygon.data.Context;
import com.zygon.data.EventFeed;
import com.zygon.data.Handler;
import com.zygon.data.FeedProvider;
import com.zygon.data.FeedProviderFactory;
import com.zygon.trade.Configuration;
import com.zygon.trade.Module;
import com.zygon.trade.Schema;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

/**
 *
 * @author zygon
 */
public class DataFeed<T> extends Module {

    private static final Schema SCHEMA = new Schema("data_feed.json");
    
    private final FeedProvider feedProviderFactory = new FeedProviderFactory();
    private final Collection<Handler<T>> feedRegistrations = Lists.newArrayList();
    private EventFeed<T> dataFeed = null;
    
    private String clazz = null;
    private String tradeable = null;
    private String currency = null;
    private String dataSetIdentifier = null; // optional
    private DataSet dataSetModule = null;
    
    public DataFeed(String name, Schema schema) {
        super (name, schema);
    }
    
    public DataFeed(String name) {
        this (name, SCHEMA);
    }

    @Override
    public void configure(Configuration configuration) {
        super.configure(configuration);
        
        // TBD: enable/disable configuration and handling already running feeds
        
        this.clazz = configuration.getValue("class");
        this.tradeable = configuration.getValue("tradeable");
        this.currency = configuration.getValue("currency");
        
        String dataSetId = configuration.getValue("data-set-identifier");
        if (dataSetId != null) {
            if (this.dataSetIdentifier == null || !dataSetId.equals(this.dataSetIdentifier)) {
                this.dataSetModule = (DataSet) this.getModule(dataSetId);
                this.dataSetIdentifier = dataSetId;
            }
        } else {
            // need to clean up old references.
            if (this.dataSetIdentifier != null) {
                this.dataSetModule = null;
                this.dataSetIdentifier = null;
            }
        }
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
        
        if (this.dataFeed == null) {
            this.dataFeed = (EventFeed<T>) this.feedProviderFactory.createFeed(ctx);
        }
        
        // load any seed data first
        if (this.dataSetModule != null) {
            com.zygon.data.set.DataSet<T> dataSet = (com.zygon.data.set.DataSet<T>) dataSetModule.getDataSet();
            for (Handler<T> reg : this.feedRegistrations) {
                try {
                    dataSet.writeData(reg);
                } catch (IOException io) {
                    this.getLogger().error(null, io);
                    // TODO: alarm of some kind?
                }
            }
        }
        
        for (Handler<T> reg : this.feedRegistrations) {
            this.dataFeed.register(reg);
        }
    }
    
    public void register (Handler<T> reg) {
        this.feedRegistrations.add(reg);
    }

    @Override
    public void uninitialize() {
        for (Handler<T> reg : this.feedRegistrations) {
            this.dataFeed.unregister(reg);
        }
    }
    
    public void unregister (Handler<T> reg) {
        this.feedRegistrations.remove(reg);
    }
}
