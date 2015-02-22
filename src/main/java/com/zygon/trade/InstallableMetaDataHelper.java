
package com.zygon.trade;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.zygon.configuration.Configuration;
import java.util.Map;

/**
 * 
 * This wraps the Metadata object because (as far as I can tell) it won't be able
 to construct a subclass of InstallableMetaData during normal processing.
 *
 * @author zygon
 */
public class InstallableMetaDataHelper {

    public static final String CLASS = "class";
    
    public static InstallableMetaData createServerMetaProperties(String id, Configuration configuration, String clazz) {
        Map<String, String> properties = Maps.newHashMap();
        
        properties.put(CLASS, clazz);
        
        return new InstallableMetaData(id, configuration, properties);
    }
    
    private final InstallableMetaData metaData;

    public InstallableMetaDataHelper(InstallableMetaData metaData) {
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
