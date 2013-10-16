
package com.zygon.schema;

/**
 *
 * @author david.charubini
 */
public class Schema {
    
    private final String draft;
    private final SchemaElement element;

    public Schema(String draft, SchemaElement element) {
        this.draft = draft;
        this.element = element;
    }
    
    public String getDescription() {
        return this.getElement().getDescription();
    }
    
    public String getDraft() {
        return this.draft;
    }

    public SchemaElement getElement() {
        return this.element;
    }
    
    public String getTitle() {
        return this.getElement().getId();
    }
}
