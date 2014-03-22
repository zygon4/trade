
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class PropertiesSchemaElement extends SchemaElement {

    private final SchemaElement[] possibleElements;
    private final String[] requiredElementIdentifiers;
    
    public PropertiesSchemaElement(String description, SchemaElement[] possibleElements, String[] requiredElementIdentifiers) {
        super("properties", Type.OBJECT, description);
        
        this.possibleElements = possibleElements;
        this.requiredElementIdentifiers = requiredElementIdentifiers;
    }

    public SchemaElement[] getElements() {
        return this.possibleElements;
    }

    public String[] getRequiredElementIdentifiers() {
        return this.requiredElementIdentifiers;
    }
}
