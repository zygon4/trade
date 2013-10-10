
package com.zygon.trade;

/**
 *
 * @author zygon
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
