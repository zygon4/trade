
package com.zygon.configuration;

/**
 * This was another avenue to configuration.  It should probably be shut down.
 * Default values and options are being taken care of in schema. Maybe eventually
 * this could become the configuration DAO component?
 * 
 * @author zygon
 */
/*pkg*/ final class Property {
    private final String name;
    private final String defaultValue;
    private final String[] options;
    
    public Property(String name, String defaultValue, String[] options) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.options = options;
    }
    
    public Property(String name, String defaultValue) {
        this(name, defaultValue, null);
    }
    
    public Property(String name, String[] options) {
        this (name, null, options);
    }
    
    public Property(String name) {
        this (name, null, null);
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public String getName() {
        return this.name;
    }

    public String[] getOptions() {
        return this.options;
    }

    public boolean hasOptions() {
        return this.options != null;
    }
    
    public boolean hasDefault() {
        return this.defaultValue != null;
    }
}
