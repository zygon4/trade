
package com.zygon.schema;

/**
 *
 * @author zygon
 */
public class BooleanSchemaElement extends PrimativeSchemaElement {

    public BooleanSchemaElement(String id, String description, boolean defaultValue) {
        super(id, Type.BOOLEAN, description, String.valueOf(defaultValue));
    }
}
