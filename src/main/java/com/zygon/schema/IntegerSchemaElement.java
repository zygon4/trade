
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class IntegerSchemaElement extends NumericSchemaElement {

    public IntegerSchemaElement(String id, String description, long defaultValue, 
            Integer min, Integer max, boolean exclusiveMinimum) {
        super(id, Type.INTEGER, description, String.valueOf(defaultValue), min, max, exclusiveMinimum);
    }
    
    public IntegerSchemaElement(String id, String description, long defaultValue, 
            Integer min, Integer max) {
        this(id, description, defaultValue, min, max, false);
    }
}
