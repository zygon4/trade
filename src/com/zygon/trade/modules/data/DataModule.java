/**
 * 
 */

package com.zygon.trade.modules.data;

import com.xeiam.xchange.currency.CurrencyPair;
import com.zygon.data.EventFeed;
import com.zygon.trade.Configuration;
import com.zygon.trade.Module;
import com.zygon.trade.ParentModule;

/**
 *
 * @author zygon
 */
public class DataModule extends ParentModule {

    public static final String ID = "data";
    
    private static DataFeed get(String clazz, String tradeable, String currency) {
        
        DataFeed feed = new DataFeed(tradeable + "_" + currency);
        
        feed.configure(new Configuration(feed.getSchema()));
        feed.getConfiguration().setValue("class", clazz);
        feed.getConfiguration().setValue("tradeable", tradeable);
        feed.getConfiguration().setValue("currency", currency);
        
        return feed;
    }
    
    private static DataFeed[] get(CurrencyPair[] pairs) {
        
        DataFeed[] feeds = new DataFeed[pairs.length];
        
        int i = 0;
        
        for (CurrencyPair pair : pairs) {
            feeds[i++] = get("com.zygon.trade.market.data.mtgox.MtGoxFeed", pair.baseCurrency, pair.counterCurrency);
        }
        
        return feeds;
    }
    
    public DataModule() {
        super (ID, null, DataFeed.class);
    }
    
    @Override
    public void initialize() {
        
    }

    public void register (EventFeed.Handler reg) {
        for (Module child : this.getModules()) {
            DataFeed feed = (DataFeed) child;
            feed.register(reg);
        }
    }
    
    @Override
    public void uninitialize() {
        
    }
    
    public void unregister (EventFeed.Handler reg) {
        for (Module child : this.getModules()) {
            DataFeed feed = (DataFeed) child;
            feed.unregister(reg);
        }
    } 
}
