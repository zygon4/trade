
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class IntegerSchemaElement extends NumericSchemaElement {

    public IntegerSchemaElement(String id, String description, int min, int max, boolean exclusiveMinimum) {
        super(id, "integer", description, min, max, exclusiveMinimum);
    }
}
