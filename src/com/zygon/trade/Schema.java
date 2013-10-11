
package com.zygon.trade;

/**
 *
 * @author zygon
 * 
 * TODO: make schema flat file based - json?
 */
public class Schema {
    
    private final Property[] properties;
    
    public Schema(Property[] properties) {
        this.properties = properties;
    }

    public Property[] getProperties() {
        return this.properties;
    }
}
