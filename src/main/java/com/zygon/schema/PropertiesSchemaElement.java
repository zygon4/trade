
package com.zygon.schema;

import java.util.Map;
import java.util.Set;

/**
 * 
 * This abstraction is starting to lose water. We need to nest Ids probably
 * using a parent concept.
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

    @Override
    public void addSchemaElements(Map<String, String> schemaElements) {
        // I don't have a default - but all of these children elements might
        for (SchemaElement element : this.possibleElements) {
            element.addSchemaElements(schemaElements);
        }
    }

    public SchemaElement[] getElements() {
        return this.possibleElements;
    }

    public String[] getRequiredElementIdentifiers() {
        return this.requiredElementIdentifiers;
    }
}
