
package com.zygon.trade.modules.data;

import com.zygon.data.Context;
import com.zygon.configuration.Configuration;
import com.zygon.schema.StringElement;
import com.zygon.schema.parse.ConfigurationSchema;
import com.zygon.trade.Module;
import com.zygon.trade.Schema;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.TickerReader;
import com.zygon.trade.modules.core.PlaygroundModule;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 *
 * @author zygon
 */
public class DataSet extends Module {

    private static Schema createSchema() {
        
        StringElement connections = new StringElement("data-uri", "URI of the data set ", "");
        ConfigurationSchema configSchema = new ConfigurationSchema(PlaygroundModule.class.getCanonicalName()+"_schema", "v1", connections);
        
        return new Schema(configSchema);
    }
    
    private String dataURI = null;
    // hardcoded to Ticker for now
    private com.zygon.data.set.DataSet<Ticker> dataSet = null;
    
    public DataSet(String name) {
        // TODO: schema
        /*
         * Has to support:
         * 
         * - Flat files in some format
         * - mysql given db URI and query
         * 
         */
        super (name, createSchema());
    }

    @Override
    public void configure(Configuration configuration) {
        super.configure(configuration);
        
        String uri = configuration.getStringValue("data-uri");
        
        if (this.dataURI != null && !uri.equals(this.dataURI)) {
            throw new UnsupportedOperationException("TODO: edit");
        }
        
        URI dataSetURI = null;
        
        try {
            dataSetURI = new URI(uri);
        } catch (URISyntaxException uriSyntax) {
            throw new IllegalArgumentException(uriSyntax);
        }
        
        this.dataURI = uri;
        
        if (dataSetURI.getScheme().equals("file")) {
            Properties props = new Properties();
            props.put(Context.PROP_NAME, this.getDisplayname());
            props.put(Context.PROP_CLS, "");// TODO:
            Context ctx = new Context(props);
            this.dataSet = new com.zygon.data.set.DataSet<Ticker>(ctx, new TickerReader(new File(dataSetURI).getAbsolutePath()), null, null);
        }
    }

    /*pkg*/ com.zygon.data.set.DataSet<?> getDataSet() {
        return this.dataSet;
    }

    @Override
    public void uninstall() {
        this.dataSet.delete();
    }

    @Override
    public void uninitialize() {
        
    }
    
//
//    @Override
//    protected Properties getDataFeedProperties(String cls, String tradeable, String currency) {
//        Properties properties = super.getDataFeedProperties(cls, tradeable, currency);
//        
//        properties.put("uri", this.dataURI);
//        
//        return properties;

    @Override
    public void initialize() {
        
    }
}
