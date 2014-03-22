
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class SchemaElement {
    
    private final String id;
    private final Type type;
    private final String description;

    public SchemaElement(String id, Type type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public final String getId() {
        return this.id;
    }

    public final Type getType() {
        return this.type;
    }
}
