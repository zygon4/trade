
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class NumericSchemaElement extends PrimativeSchemaElement {

    private final int min; 
    private final int max;
    private final boolean exclusiveMinimum;
    
    public NumericSchemaElement(String id, String description, int min, int max, boolean exclusiveMinimum) {
        super(id, "number", description);
        
        this.min = min;
        this.max = max;
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public boolean isExclusiveMinimum() {
        return exclusiveMinimum;
    }
}
