
package com.zygon.schema.parse;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.zygon.schema.SchemaElement;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 *
 * @author david.charubini
 */
public class ConfigurationSchema {
    
    private final String schemaResourceName;
    private final String draft;
    private final SchemaElement element;

    public ConfigurationSchema(String schemaResourceName, String draft, SchemaElement element) {
        Preconditions.checkNotNull(schemaResourceName);
        Preconditions.checkNotNull(draft);
        Preconditions.checkNotNull(element);
        
        this.schemaResourceName = schemaResourceName;
        this.draft = draft;
        this.element = element;
    }
    
    /**
     * Returns all of the configuration schema ids and their respective default
     * values (if present).
     * @return all of the configuration schema ids and their respective default
     * values (if present).
     */
    public Map<String, String> getSchemaElements() {
        Map<String, String> schemaElements = Maps.newHashMap();
        
        this.element.addSchemaElements(schemaElements);
        
        return schemaElements;
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
