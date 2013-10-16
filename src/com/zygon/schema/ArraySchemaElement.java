
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class ArraySchemaElement extends SchemaElement {

    private final SchemaElement[] elements;
    private final int minElements;
    private final boolean[] uniqueElements;
    
    public ArraySchemaElement(String description, SchemaElement[] elements, int minElements, boolean[] uniqueElements) {
        super("items", "array", description);
        
        this.elements = elements;
        this.minElements = minElements;
        this.uniqueElements = uniqueElements;
    }

    public ArraySchemaElement(String description, SchemaElement[] elements, boolean[] uniqueElements) {
        this(description, elements, 0, uniqueElements);
    }
    
    public ArraySchemaElement(String description, SchemaElement[] elements) {
        this(description, elements, 0, null);
    }
    
    public SchemaElement[] getElements() {
        return this.elements;
    }

    public int getMinElements() {
        return this.minElements;
    }
}
