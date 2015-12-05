
package com.zygon.configuration;

import java.io.File;
import java.io.IOException;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

/**
 *
 * @author zygon
 */
public class ApacheConfigTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ConfigurationException, IOException {
        
        Parameters params = new Parameters();
        // Read data from this file
        File propertiesFile = new File("/tmp/config.properties.xml");
        propertiesFile.createNewFile();
        
        FileBasedConfigurationBuilder<XMLConfiguration> builder
                = new FileBasedConfigurationBuilder<>(XMLConfiguration.class)
                .configure(params.xml().setFile(propertiesFile).setValidating(true));
        
        XMLConfiguration config = null;
        
        try {
            config = builder.getConfiguration();
            
//            config.addProperty("name", "joe");
//            config.addProperty("version", 1);
//            config.addProperty("property", new Property("prop-name", "default-value-1"));
//            config.addProperty("no-default", "");
            
        } catch (ConfigurationException cex) {
            // loading of the configuration file failed
            cex.printStackTrace();
        }
        
        FileHandler handler = new FileHandler(config);
        
        handler.save(propertiesFile);
        
        System.out.println("name:" + config.getString("name"));
        System.out.println("version:" + config.getInt("version"));
        
        
    }
}
