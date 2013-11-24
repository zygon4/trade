
package com.zygon.trade.modules.data;

import com.zygon.trade.Configuration;
import com.zygon.trade.Module;

/**
 *
 * @author zygon
 */
public class DataSet extends Module {

    private String dataURI = null;
    
    public DataSet(String name) {
        // TODO: schema
        super (name, null);
    }

    @Override
    public void configure(Configuration configuration) {
        super.configure(configuration);
        
        String uri = configuration.getValue("data-uri");
        
        if (!uri.equals(this.dataURI)) {
            // TODO:
            throw new UnsupportedOperationException("TODO: edit");
        }
        
        this.dataURI = uri;
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
