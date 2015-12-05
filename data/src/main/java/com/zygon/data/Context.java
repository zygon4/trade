
package com.zygon.data;

import java.util.Properties;

/**
 *
 * @author david.charubini
 */
public class Context {
    
    public static final String PROP_CLS = "class";
    public static final String PROP_NAME = "name";

    private final Properties properties = new Properties();

    public Context(Properties properties) {
        // Copy in the properties.  If it had defaults... well.. we shouldn't 
        // be using
        for (String key : properties.stringPropertyNames()) {
            this.properties.setProperty(key, properties.getProperty(key));
        }
        
        if (this.getClazz() == null) {
            throw new IllegalArgumentException("property 'class' is required");
        }
    }

    public String getProperty(String propertyName, String defaultValue) {
        return this.properties.getProperty(propertyName, defaultValue);
    }
    
    public String getProperty(String propertyName) {
        return this.properties.getProperty(propertyName);
    }
    
    /*pkg*/ final String getClazz () {
        return this.properties.getProperty(PROP_CLS);
    }
    
    public final String getName() {
        return this.properties.getProperty(PROP_NAME, "");
    }
}
