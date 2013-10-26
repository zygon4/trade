/**
 * 
 */

package com.zygon.trade.modules.data;

import com.zygon.trade.Configuration;
import com.zygon.trade.Module;
import com.zygon.trade.Schema;
import com.zygon.trade.ParentModule;
import com.zygon.trade.db.Database;
import com.zygon.trade.market.data.AbstractDataProvider;
import com.zygon.trade.market.data.DataListener;
import com.zygon.trade.market.data.DataLogger;
import com.zygon.trade.market.data.DataProvider;
import com.zygon.trade.market.data.PersistentDataLogger;
import com.zygon.trade.modules.core.DBModule;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zygon
 */
public class DataModule extends ParentModule {

    private final DataListener listener = null;
    
    private static Schema SCHEMA = new Schema("data_schema.json");

    private final FeedModule mtGoxTicker = new FeedModule("mtgox-ticker");
    
    public DataModule() {
        super ("data", SCHEMA, FeedModule.class);
        
        this.mtGoxTicker.configure(new Configuration(this.mtGoxTicker.getSchema()));
        this.mtGoxTicker.getConfiguration().setValue("class", "com.zygon.trade.market.data.mtgox.MtGoxFeed");
        this.mtGoxTicker.getConfiguration().setValue("tradeable", "BTC");
        this.mtGoxTicker.getConfiguration().setValue("currency", "USD");
    }
    
    public DataListener getDataManager() {
        return this.listener;
    }
    
    @Override
    public Module[] getModules() {
        return new Module[]{this.mtGoxTicker};
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

    @Override
    public void uninitialize() {
        this.listener.unintialize();
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
