
package com.zygon.data;

import java.util.Properties;

/**
 *
 * @author david.charubini
 */
public class Context {
    
    public static final String PROP_CLS = "class";
    public static final String PROP_NAME = "name";

    private final Properties properties;

    public Context(Properties properties) {
        this.properties = properties;
        
        if (this.getClazz() == null) {
            throw new IllegalArgumentException("property 'class' is required");
        }
    }

    public Properties getProperties() {
        return this.properties;
    }
    
    /*pkg*/ final String getClazz () {
        return this.properties.getProperty(PROP_CLS);
    }
    
    public final String getName() {
        return this.properties.getProperty(PROP_NAME, "");
    }
}
