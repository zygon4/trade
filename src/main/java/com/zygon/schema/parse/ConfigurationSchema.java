
package com.zygon.schema.parse;

import com.zygon.schema.SchemaElement;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author david.charubini
 */
public class ConfigurationSchema {
    
    private final String schemaResourceName;
    private final String draft;
    private final SchemaElement element;

    public ConfigurationSchema(String schemaResourceName, String draft, SchemaElement element) {
        this.schemaResourceName = schemaResourceName;
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

    public String getSchemaResourceName() {
        return this.schemaResourceName;
    }
    
    public static ConfigurationSchema parse (String schemaResourceName, InputStream is, SchemaParser parser) throws IOException {
        return parser.parse(schemaResourceName, is);
    }
}
