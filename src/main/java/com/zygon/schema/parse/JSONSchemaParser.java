
package com.zygon.schema.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.zygon.schema.ArraySchemaElement;
import com.zygon.schema.ConfigurationSchema;
import com.zygon.schema.IntegerSchemaElement;
import com.zygon.schema.NumericSchemaElement;
import com.zygon.schema.PropertiesSchemaElement;
import com.zygon.schema.SchemaElement;
import com.zygon.schema.StringElement;
import com.zygon.schema.Type;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zygon
 */
public class JSONSchemaParser implements SchemaParser {
    
    private static enum Keyword {
        ARRAY ("array"),
        EXCLUSIVE_MINIMUM ("exclusiveMinimum"),
        SCHEMA ("$schema"),
        TITLE ("title"),
        DESC ("description"),
        ITEMS ("items"),
        MAXIMUM ("maximum"),
        MIN_ITEMS ("minItems"),
        MINIMUM ("minimum"),
        PROPERTIES ("properties"),
        TYPE ("type"),
        REQUIRED ("required"),
        UNIQUE ("uniqueItems");
        
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
    
    // TODO: unfinished
    private ArraySchemaElement parseArray (String title, String description, Map<String, Object> props) {
        /*
         "tags": {
            "type": "array",
            "items": {
                "type": "string"
            },
            "minItems": 1,
            "uniqueItems": true
        }
         */
        
        if (props.containsKey(Keyword.TITLE.getVal())) {
            title = (String) props.get(Keyword.TITLE.getVal());
        }
        
        if (props.containsKey(Keyword.DESC.getVal())) {
            description = (String) props.get(Keyword.DESC.getVal());
        }
        
        int minItems = 0;
        
        if (props.containsKey(Keyword.MIN_ITEMS.getVal())) {
            minItems = (int) props.get(Keyword.MIN_ITEMS.getVal());
        }
        
        boolean uniqueItems = false;
        
        if (props.containsKey(Keyword.UNIQUE.getVal())) {
            uniqueItems = (boolean) props.get(Keyword.UNIQUE.getVal());
        }
        
        SchemaElement element = null;
        
        if (props.containsKey(Keyword.ITEMS.getVal())) {
            Map<String, Object> properties = (Map<String, Object>) props.get(Keyword.ITEMS.getVal());
            element = this.parse(properties, title);
        } else {
            throw new IllegalStateException();
        }
        
        return new ArraySchemaElement(title, description, element, minItems, uniqueItems);
    }
    
    private PropertiesSchemaElement parseProperties (String title, String description, Map<String, Object> props) {
        
        /*
        "type": "object",
        "properties": {
            "id": {
                "description": "The unique identifier for a product",
                "type": "integer"
            },
            "name": {
                "description": "Name of the product",
                "type": "string"
            },
        }
        "required": ["id", "name"]
        */
        
        String[] required = null;
        
        if (props.containsKey(Keyword.REQUIRED.getVal())) {
            List<String> req = (List<String>) props.get(Keyword.REQUIRED.getVal());
            required = req.toArray(new String[req.size()]);
        }
        
        SchemaElement[] elements = null;
        
        if (props.containsKey(Keyword.PROPERTIES.getVal())) {
            List<SchemaElement> elems = Lists.newArrayList();
            Map<String, Object> properties = (Map<String, Object>) props.get(Keyword.PROPERTIES.getVal());
            
            for (String key : properties.keySet()) {
                elems.add(this.parse((Map<String,Object>)properties.get(key), key));
            }
            
            elements = elems.toArray(new SchemaElement[elems.size()]);
        } else {
            throw new IllegalStateException();
        }
        
        return new PropertiesSchemaElement(description, elements, required);
    }
    
    private IntegerSchemaElement parseInteger (String title, String description, Map<String, Object> props) {
        
//        "price": {
//            "type": "number",
//            "minimum": 0,
//            "exclusiveMinimum": true
//        },
        
        Integer min = null;
        
        if (props.containsKey(Keyword.MINIMUM.getVal())) {
            min = (int) props.get(Keyword.MINIMUM.getVal());
        }
        
        Integer max = null;
        
        if (props.containsKey(Keyword.MAXIMUM.getVal())) {
            max = (int) props.get(Keyword.MAXIMUM.getVal());
        }
        
        boolean exclusiveMin = false;
        
        if (props.containsKey(Keyword.EXCLUSIVE_MINIMUM.getVal())) {
            exclusiveMin = (boolean) props.get(Keyword.EXCLUSIVE_MINIMUM.getVal());
        }
        
        return new IntegerSchemaElement(title, description, min, max, exclusiveMin);
    }
    
    private NumericSchemaElement parseNumeric (String title, String description, Map<String, Object> props) {
        
//        "price": {
//            "type": "number",
//            "minimum": 0,
//            "exclusiveMinimum": true
//        },
        
        int min = 0;
        
        if (props.containsKey(Keyword.MINIMUM.getVal())) {
            min = (int) props.get(Keyword.MINIMUM.getVal());
        }
        
        int max = 0;
        
        if (props.containsKey(Keyword.MAXIMUM.getVal())) {
            max = (int) props.get(Keyword.MAXIMUM.getVal());
        }
        
        boolean exclusiveMin = false;
        
        if (props.containsKey(Keyword.EXCLUSIVE_MINIMUM.getVal())) {
            exclusiveMin = (boolean) props.get(Keyword.EXCLUSIVE_MINIMUM.getVal());
        }
        
        return new NumericSchemaElement(title, description, min, max, exclusiveMin);
    }
    
    private StringElement parseString (String title, String description, Map<String, Object> props) {
        // TBD: regex, constraints, etc
        return new StringElement(title, description);
    }
    
    private SchemaElement parse (Map<String, Object> props, String id) {
        
        String desc = (String) props.get(Keyword.DESC.getVal());
        Type type = Type.instance((String) props.get(Keyword.TYPE.getVal()));
        
        SchemaElement element = null;
        
        switch (type) {
            case ARRAY:
                element = parseArray(id, desc, props);
                break;
            case INTEGER:
                element = parseInteger(id, desc, props);
                break;
            case NUMBER:
                element = parseNumeric(id, desc, props);
                break;
            case OBJECT:
                element = parseProperties(id, desc, props);
                break;
            case STRING:
                element = parseString(id, desc, props);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        
        return element;
    }
    
    private SchemaElement parse (Map<String, Object> props) {
        String id = (String) props.get(Keyword.TITLE.getVal());
        return this.parse (props, id);
    }
    
    @Override
    public ConfigurationSchema parse(String schemaResourceName, InputStream is) throws IOException {
        
        Map<String, Object> props = (Map<String,Object>) new ObjectMapper().readValue(is, HashMap.class);
        
        String schema = (String) props.get(Keyword.SCHEMA.getVal());
        SchemaElement element = parse(props);
        
        return new ConfigurationSchema(schemaResourceName, schema, element);
    }
}
