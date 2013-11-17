/**
 * 
 */

package com.zygon.trade.modules.data;

import com.xeiam.xchange.currency.CurrencyPair;
import com.zygon.data.EventFeed;
import com.zygon.trade.Configuration;
import com.zygon.trade.Module;
import com.zygon.trade.Schema;
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
    
    private static Schema SCHEMA = new Schema("data_schema.json");

    private final FeedModule[] feeds;
    
    private static FeedModule get(String clazz, String tradeable, String currency) {
        
        FeedModule feed = new FeedModule(tradeable + "_" + currency);
        
        feed.configure(new Configuration(feed.getSchema()));
        feed.getConfiguration().setValue("class", clazz);
        feed.getConfiguration().setValue("tradeable", tradeable);
        feed.getConfiguration().setValue("currency", currency);
        
        return feed;
    }
    
    private static FeedModule[] get(CurrencyPair[] pairs) {
        
        FeedModule[] feeds = new FeedModule[pairs.length];
        
        int i = 0;
        
        for (CurrencyPair pair : pairs) {
            feeds[i++] = get("com.zygon.trade.market.data.mtgox.MtGoxFeed", pair.baseCurrency, pair.counterCurrency);
        }
        
        return feeds;
    }
    
    private static final CurrencyPair[] PAIRS = new CurrencyPair[]{
        CurrencyPair.BTC_USD,
        CurrencyPair.BTC_GBP,
        CurrencyPair.BTC_EUR,
        CurrencyPair.BTC_JPY,
        CurrencyPair.BTC_CHF,
        CurrencyPair.BTC_AUD,
        CurrencyPair.BTC_CAD
    };
    
    public DataModule() {
        super (ID, SCHEMA, FeedModule.class);
        this.feeds = get(PAIRS);
    }
    
    @Override
    public Module[] getModules() {
        return this.feeds;
    }

    @Override
    public void initialize() {
        // TBD: rejigger data module
//        DBModule dbModule = (DBModule) this.getModule(DBModule.ID);
        
//        Database db = dbModule.getDatabase();
//        
//        // Boo hiss. These should be removed and a cleaner way to provide 
//        // database access should be implemented.
//        
//        DataLogger dataLogger = this.listener.getDataLogger();
//        if (dataLogger != null && dataLogger instanceof PersistentDataLogger) {
//            ((PersistentDataLogger)dataLogger).setDatabase(db);
//        }
//        
//        DataProvider dataProvider = this.listener.getDataProvider();
//        if (dataProvider != null && dataProvider instanceof AbstractDataProvider) {
//            ((AbstractDataProvider) this.listener.getDataProvider()).setDatabase(db);
//        }
//        
//        this.listener.initialize();
    }

    public void register (EventFeed.Handler reg) {
        for (FeedModule feed : this.feeds) {
            feed.register(reg);
        }
    }
    
    
    @Override
    public void uninitialize() {
        
    }
    
    public void unregister (EventFeed.Handler reg) {
        for (FeedModule feed : this.feeds) {
            feed.unregister(reg);
        }
    }
    
    public static void main (String[] args) throws IOException, URISyntaxException {
        
        Map<String,Object> userData = new HashMap<String,Object>();
        Map<String,String> nameStruct = new HashMap<String,String>();
        nameStruct.put("first", "Joe");
        nameStruct.put("last", "Sixpack");
        userData.put("name", nameStruct);
        userData.put("gender", "MALE");
        userData.put("verified", Boolean.FALSE);
        userData.put("userImage", "Rm9vYmFyIQ==");

        
    }
    
}
