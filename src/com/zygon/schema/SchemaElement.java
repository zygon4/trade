
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class SchemaElement {
    
    private final String id;
    private final String type;
    private final String description;

    public SchemaElement(String id, String type, String description) {
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

    public final String getType() {
        return this.type;
    }
}
