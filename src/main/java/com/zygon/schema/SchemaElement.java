
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class SchemaElement {
    
    private final String id;
    private final Type type;
    private final String description;
    private final String defaultValue;

    // Dot '.' may become a forbidden character - we may have to use
    // something to delineate nested schemas
    public SchemaElement(String id, Type type, String description, String defaultValue) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.defaultValue = defaultValue;
    }
    
    public SchemaElement(String id, Type type, String description) {
        this(id, type, description, null);
    }

    public String getDefaultValue() {
        return this.defaultValue;
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
