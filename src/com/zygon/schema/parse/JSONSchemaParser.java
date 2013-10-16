
package com.zygon.schema.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zygon.schema.PropertiesSchemaElement;
import com.zygon.schema.Schema;
import com.zygon.schema.SchemaElement;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author zygon
 */
public class JSONSchemaParser implements SchemaParser {

    private static enum Type {
        ARRAY ("array"),
        OBJECT ("object");
        
        private final String val;

        private Type(String val) {
            this.val = val;
        }

        public String getVal() {
            return this.val;
        }
        
        private static Type instance(String val) {
            for (Type type : Type.values()) {
                if (type.getVal().equals(val)) {
                    return type;
                }
            }
            
            return null;
        }
    }
    
    private static enum Keyword {
        ARRAY ("array"),
        SCHEMA ("$schema"),
        TITLE ("title"),
        DESC ("description"),
        PROPERTIES ("properties"),
        TYPE ("type"),
        REQUIRED ("required");
        
        private final String val;

        private Keyword(String val) {
            this.val = val;
        }

        public String getVal() {
            return this.val;
        }
        
        private static Keyword instance(String val) {
            for (Keyword kw : Keyword.values()) {
                if (kw.getVal().equals(val)) {
                    return kw;
                }
            }
            
            return null;
        }
    }
    
    private static final String PROP_SCHEMA = "$schema";
    private static final String PROP_TITLE = "title";
    private static final String PROP_DESC = "description";
    private static final String PROP_TYPE = "type";
    private static final String PROP_REQUIRED = "required";
    
    
    private static final String TYPE_OBJECT = "object";
    private static final String TYPE_ARRAY = "array";
    
    private SchemaElement parse (String key, Object prop) {
        String id = null;
        String desc = null;
        Type type = null;
        
        
        
        return null;
    }
    
    private SchemaElement parse (HashMap<String, Object> props) {
//        String id = props.get(id);
        String desc = null;
        Type type = null;
        
        
        
        return null;
    }
    
    @Override
    public Schema parse(InputStream is) throws IOException {
        
        HashMap<String, Object> props = (HashMap<String,Object>) new ObjectMapper().readValue(is, HashMap.class);
        
//        SchemaElement element = this.parse(null, props.)
        
        String schema = null;
        String title = null;
        String desc = null;
        
        Type type = null;
        List<String> required = null;
        List<SchemaElement> elements = new ArrayList<>();
        
        for (String key : props.keySet()) {
            Keyword kw = Keyword.instance(key);
                
            switch (kw) {
                case ARRAY:
                    if (!elements.isEmpty()) {
                        throw new IllegalStateException();
                    }
                    throw new UnsupportedOperationException();
                case DESC:
                    desc = (String) props.get(key);
                    break;
                case PROPERTIES:
                    if (!elements.isEmpty()) {
                        throw new IllegalStateException();
                    }
                    
                    HashMap<String, Object> properties = (HashMap<String, Object>) props.get(key);
                    
                    for (String propertyName : properties.keySet()) {
                        elements.add(parse(propertyName, properties.get(propertyName)));
                    }
                    
                    break;
                case REQUIRED:
                    required = (List<String>) props.get(key);
                    break;
                case SCHEMA:
                    schema = (String) props.get(key);
                    break;
                case TITLE:
                    title = (String) props.get(key);
                    break;
                case TYPE:
                    type = Type.instance((String) props.get(key));
                    break;
            }
        }
        
        if (schema == null || title == null || type == null || elements.isEmpty()) {
            // TODO: why
            throw new IllegalArgumentException("invalid schema");
        }
        
        SchemaElement element = null;
        switch (type) {
            case ARRAY:
                throw new UnsupportedOperationException();
            case OBJECT:
//                element = new PropertiesSchemaElement(elements.toArray(new SchemaElement[elements.size()]), required.toArray(new String[required.size()]));
                break;
        }
        
        return new Schema(schema, element);
    }
}
