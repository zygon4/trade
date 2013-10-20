
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class StringElement extends PrimativeSchemaElement {

    public StringElement(String id, String description) {
        super(id, Type.STRING, description);
    }

    // TBD: regex? min/max length?
}
