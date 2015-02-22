
package com.zygon.configuration;

/**
 *
 * @author david.charubini
 */
public class MetaData {
    
    private final String id;
    private final String clazz;
    private final Configuration configuration;

    public MetaData(String id, String clazz, Configuration configuration) {
        this.id = id;
        this.clazz = clazz;
        this.configuration = configuration;
    }

    public String getClazz() {
        return this.clazz;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public String getId() {
        return this.id;
    }
}
