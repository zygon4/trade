
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class IntegerSchemaElement extends NumericSchemaElement {

    public IntegerSchemaElement(String id, String description, Integer min, Integer max, boolean exclusiveMinimum) {
        super(id, Type.INTEGER, description, min, max, exclusiveMinimum);
    }
}
