
package com.zygon.schema;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Set;

/**
 * TBD: consider setting a package-scoped parent for Id nesting
 *
 * @author david.charubini
 */
public class SchemaElement {
    
    private final String id;
    private final Type type;
    private final String description;
    private final String defaultValue;

    // TBD: "id" may not be strong enough. Compound schemas may require
    // nested, multi-named elements which could require magical appending
    // of names for querying deeper within the schema tree.
    
    // TBD: Dot '.' may become a forbidden character - we may have to use
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

    /**
     * 
     * @param schemaElements 
     */
    public void addSchemaElements(Map<String, String> schemaElements) {
        Preconditions.checkState(!schemaElements.containsKey(this.getId()));
        schemaElements.put(this.getId(), this.hasDefault() ? this.getDefaultValue() : null);
    }
    
    public final String getDefaultValue() {
        return this.defaultValue;
    }

    public final String getDescription() {
        return this.description;
    }

    public final String getId() {
        return this.id;
    }

    public final Type getType() {
        return this.type;
    }
    
    public final boolean hasDefault() {
        return this.defaultValue != null;
    }
}
