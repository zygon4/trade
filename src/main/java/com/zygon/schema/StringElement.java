
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class StringElement extends PrimativeSchemaElement {

    public StringElement(String id, String description, String defaultValue) {
        super(id, Type.STRING, description, defaultValue);
    }

    // TBD: regex? min/max length?
}
