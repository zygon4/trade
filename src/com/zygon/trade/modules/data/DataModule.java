/**
 * 
 */

package com.zygon.trade.modules.data;

import com.xeiam.xchange.currency.CurrencyPair;
import com.zygon.data.EventFeed;
import com.zygon.trade.Configuration;
import com.zygon.trade.Module;
import com.zygon.trade.ParentModule;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zygon
 */
public class DataModule extends ParentModule {

    public static final String ID = "data";
    
    private final DataFeed[] feeds;
    
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
    
    private static final CurrencyPair[] PAIRS = new CurrencyPair[]{
        CurrencyPair.BTC_USD,
//        CurrencyPair.BTC_GBP,
//        CurrencyPair.BTC_EUR,
//        CurrencyPair.BTC_JPY,
//        CurrencyPair.BTC_CHF,
//        CurrencyPair.BTC_AUD,
//        CurrencyPair.BTC_CAD
    };
    
    public DataModule() {
        super (ID, null, DataFeed.class);
        this.feeds = get(PAIRS);
    }
    
    @Override
    public Module[] getModules() {
        return this.feeds;
    }

    @Override
    public void initialize() {
        
    }

    public void register (EventFeed.Handler reg) {
        for (DataFeed feed : this.feeds) {
            feed.register(reg);
        }
    }
    
    
    @Override
    public void uninitialize() {
        
    }
    
    public void unregister (EventFeed.Handler reg) {
        for (DataFeed feed : this.feeds) {
            feed.unregister(reg);
        }
    } 
}
