
package com.zygon.configuration;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.io.FileHandler;

/**
 * Based on flat files for now
 * 
 * TODO: test, consider XML format
 *
 * @author zygon
 */
public class ApacheConfiguration {
    
    private static final String CONFIG_PATH = File.pathSeparator + "tmp"; // Sure, why not for now
    
    private static String createConfigurationFileLocation(UUID elementUUID) {
        return CONFIG_PATH + File.pathSeparator + elementUUID.toString() + ".cfg";
    }
    
    private static FileBasedConfiguration createConfiguration(File propertiesFile) throws ConfigurationException {
        try {
            return new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                    .configure(new Parameters().fileBased().setFile(propertiesFile)).getConfiguration();
        } catch (org.apache.commons.configuration2.ex.ConfigurationException ce) {
            throw new ConfigurationException(ce);
        }
    }
    
    private static FileBasedConfiguration createConfiguration(File propertiesFile, Collection<Property> properties) throws ConfigurationException {
        
        FileBasedConfiguration config = null;
        
        try {
            config = createConfiguration(propertiesFile);
            
            for (Property prop : properties) {
                config.addProperty(prop.getName(), "");
                // Add separate key/val for default value "<PROP>.default" has 
                // to be the known format.  This might fault funky behaviors
                // if properties aren't cleaned enough. TBD: worry about later.
                
                if (prop.hasDefault()) {
                    config.addProperty(prop.getName()+".default", prop.getDefaultValue());
                }
                
                // TBD: key/val other property attributes?
            }
        
            // Write out the properties
            FileHandler handler = new FileHandler(config);
            handler.save(propertiesFile);
            
        } catch (org.apache.commons.configuration2.ex.ConfigurationException cex) {
            // This is bad - probably unrecoverable
            throw new ConfigurationException(cex);
        }
        
        return config;
    }
    
    /**
     * Returns a new Configuration object with the given properties persisted.
     * @param elementUUID
     * @param properties
     * @return 
     * @throws ConfigurationException 
     */
    public static ApacheConfiguration createConfiguration(UUID elementUUID, Collection<Property> properties) throws ConfigurationException {
        File createConfigPath = new File(createConfigurationFileLocation(elementUUID));
        
        Preconditions.checkState(!createConfigPath.exists());
        
        FileBasedConfiguration config = createConfiguration(createConfigPath, properties);
        
        return new ApacheConfiguration(elementUUID, properties, config);
    }
    
    /**
     * Returns the Configuration object associated with the elementUUID provided.
     * @param elementUUID
     * @return 
     * @throws com.zygon.configuration.ConfigurationException 
     */
    public static ApacheConfiguration getConfiguration(UUID elementUUID) throws ConfigurationException {
        File createConfigPath = new File(createConfigurationFileLocation(elementUUID));
        Preconditions.checkState(createConfigPath.exists());
        
        FileBasedConfiguration config = createConfiguration(createConfigPath);
        
        FileHandler handler = new FileHandler();
        
        try {
            handler.load(createConfigPath);
        } catch (org.apache.commons.configuration2.ex.ConfigurationException ce) {
            throw new ConfigurationException(ce);
        }
        
        Set<Property> properties = Sets.newHashSet();
        
        Iterator<String> keys = config.getKeys();
        while (keys.hasNext()) {
            String propKey = keys.next();
            
            String propName = null;
            String propDefault = null;
            
            // This is inefficient - skipping all the .default entries
            // even though they are probably in order to keep the logic 
            // simpler.
            if (!propKey.endsWith(".default")) {
                propName = propKey;
                
                String defaultValueKey = propKey+".default";
                Object defaultValue = config.getProperty(defaultValueKey);
                
                if (defaultValue != null) {
                    propDefault = (String) defaultValue;
                }
            }
            
            properties.add(new Property(propName, propDefault));
        }
        
        return new ApacheConfiguration(elementUUID, properties, config);
    }
    
    
    private final UUID elementUUID;
    private final Map<String, Property> propertiesByName = Maps.newHashMap();
    private final FileBasedConfiguration configuration;
    
    
    private ApacheConfiguration(UUID elementUUID, Collection<Property> properties, FileBasedConfiguration configuration) {
        Preconditions.checkNotNull(elementUUID);
        Preconditions.checkNotNull(properties);
        Preconditions.checkArgument(!properties.isEmpty());
        Preconditions.checkNotNull(configuration);
        
        this.elementUUID = elementUUID;
        
        for (Property prop : properties) {
            this.propertiesByName.put(prop.getName(), prop);
        }
        
        // If the name hashing causes us to loose a propery - there was was a dup
        Preconditions.checkArgument(this.propertiesByName.size() == properties.size());
        
        this.configuration = configuration;
    }
    
    public String getValue(String name) {
        Preconditions.checkArgument(this.propertiesByName.containsKey(name));
        
        return null;
    }
    
    public void setValue(String name, String value) {
        
    }
}
