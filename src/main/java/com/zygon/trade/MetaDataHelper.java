
package com.zygon.trade;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.zygon.configuration.Configuration;
import com.zygon.configuration.MetaData;
import java.util.Map;

/**
 * 
 * This wraps the Metadata object because (as far as I can tell) it won't be able
 * to construct a subclass of MetaData during normal processing.
 *
 * @author zygon
 */
public class MetaDataHelper {

    public static final String CLASS = "class";
    
    public static MetaData createServerMetaProperties(String id, Configuration configuration, String clazz) {
        Map<String, String> properties = Maps.newHashMap();
        
        properties.put(CLASS, clazz);
        
        return new MetaData(id, configuration, properties);
    }
    
    private final MetaData metaData;

    public MetaDataHelper(MetaData metaData) {
        Preconditions.checkNotNull(metaData);
        
        this.metaData = metaData;
    }
    
    public String getClazz() {
        return this.metaData.getProperty(CLASS);
    }
    
    public Configuration getConfiguration() {
        return this.metaData.getConfiguration();
    }
    
    public String getId() {
        return this.metaData.getId();
    }
}
