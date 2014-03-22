
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class NumericSchemaElement extends PrimativeSchemaElement {

    private final Integer min; 
    private final Integer max;
    private final boolean exclusiveMinimum;
    
    protected NumericSchemaElement(String id, Type type, String description, Integer min, Integer max, boolean exclusiveMinimum) {
        super(id, type, description);
        
        this.min = min;
        this.max = max;
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public NumericSchemaElement(String id, String description, Integer min, Integer max, boolean exclusiveMinimum) {
        this(id, Type.NUMBER, description, min, max, exclusiveMinimum);
    }
    
    public Integer getMax() {
        return this.max;
    }

    public Integer getMin() {
        return this.min;
    }

    public boolean isExclusiveMinimum() {
        return this.exclusiveMinimum;
    }
}
