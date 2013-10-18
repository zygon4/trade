
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class ArraySchemaElement extends SchemaElement {

    private final SchemaElement element;
    private final int minElements;
    private final boolean uniqueElements;
    
    public ArraySchemaElement(String title, String description, SchemaElement element, int minElements, boolean uniqueElements) {
        super(title, "array", description);
        
        this.element = element;
        this.minElements = minElements;
        this.uniqueElements = uniqueElements;
    }

    public SchemaElement getElement() {
        return this.element;
    }

    public int getMinElements() {
        return this.minElements;
    }

    public boolean isUniqueElements() {
        return this.uniqueElements;
    }
}
